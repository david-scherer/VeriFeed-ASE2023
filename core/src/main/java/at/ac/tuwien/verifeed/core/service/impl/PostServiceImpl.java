package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.dto.CommentaryDto;
import at.ac.tuwien.verifeed.core.dto.extraction.UngroupedPostCollectionDto;
import at.ac.tuwien.verifeed.core.dto.recognition.AccountDto;
import at.ac.tuwien.verifeed.core.dto.recognition.GroupedPostCollectionDto;
import at.ac.tuwien.verifeed.core.dto.recognition.ParentPostDto;
import at.ac.tuwien.verifeed.core.dto.recognition.PostDto;
import at.ac.tuwien.verifeed.core.dto.recognition.PostMetricsDto;
import at.ac.tuwien.verifeed.core.dto.recognition.RecognitionServiceRequest;
import at.ac.tuwien.verifeed.core.dto.recognition.TopicDto;
import at.ac.tuwien.verifeed.core.dto.recognition.TopicsAndPostCollectionDto;
import at.ac.tuwien.verifeed.core.entities.Blacklist;
import at.ac.tuwien.verifeed.core.entities.Commentary;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.entities.Platform;
import at.ac.tuwien.verifeed.core.entities.Posting;
import at.ac.tuwien.verifeed.core.entities.Source;
import at.ac.tuwien.verifeed.core.entities.Topic;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.repositories.BlacklistRepository;
import at.ac.tuwien.verifeed.core.repositories.CommentaryRepository;
import at.ac.tuwien.verifeed.core.repositories.PlatformRepository;
import at.ac.tuwien.verifeed.core.repositories.PostingRepository;
import at.ac.tuwien.verifeed.core.repositories.SourceRepository;
import at.ac.tuwien.verifeed.core.repositories.TopicRepository;
import at.ac.tuwien.verifeed.core.service.JournalistService;
import at.ac.tuwien.verifeed.core.service.PostService;
import at.ac.tuwien.verifeed.core.service.RecognitionServiceClient;
import at.ac.tuwien.verifeed.core.service.ScrapingServiceClient;
import at.ac.tuwien.verifeed.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PostServiceImpl implements PostService {

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostingRepository postingRepository;
    private final TopicRepository topicRepository;
    private final SourceRepository sourceRepository;
    private final PlatformRepository platformRepository;
    private final RecognitionServiceClient recognitionServiceClient;
    private final BlacklistRepository blacklistRepository;

    private final UserService userService;

    private final CommentaryRepository commentaryRepository;

    private final JournalistService journalistService;

    private final ScrapingServiceClient scrapingServiceClient;


    @Autowired
    public PostServiceImpl(
            PostingRepository postingRepository,
            TopicRepository topicRepository,
            SourceRepository sourceRepository,
            PlatformRepository platformRepository,
            RecognitionServiceClient recognitionServiceClient,
            BlacklistRepository blacklistRepository, UserService userService,
            CommentaryRepository commentaryRepository,
            JournalistService journalistService, ScrapingServiceClient scrapingServiceClient
    ) {
        this.postingRepository = postingRepository;
        this.topicRepository = topicRepository;
        this.sourceRepository = sourceRepository;
        this.platformRepository = platformRepository;
        this.recognitionServiceClient = recognitionServiceClient;
        this.blacklistRepository = blacklistRepository;
        this.userService = userService;
        this.commentaryRepository = commentaryRepository;
        this.journalistService = journalistService;
        this.scrapingServiceClient = scrapingServiceClient;
    }

    @Transactional
    @Override
    public void handleUngroupedPostCollection(UngroupedPostCollectionDto ungroupedPostCollection) {
        // Get posts from the past 5 days.
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        List<PostDto> recentPostings = this.postingRepository.findByPublishedAfter(fiveDaysAgo).stream()
                .filter(posting -> posting.getSources().isEmpty() || !blacklistRepository.isBlacklisted(posting.getSources().get(0).getLocation().toString()))
                .map(posting -> {
                    Instant instant = posting.getPublished().toInstant(ZoneOffset.UTC);
                    Date date = Date.from(instant);
                    String url = posting.getSources().isEmpty() ? "" : posting.getSources().get(0).getLocation().toString();
                    return PostDto.builder()
                            .postId("")
                            .url(url)
                            .createdAt(date)
                            .account(AccountDto.builder().accountId("").username("").createdAt(date).build())
                            .content(posting.getSummary())
                            .metrics(PostMetricsDto.builder().build())
                            .parentPost(ParentPostDto.builder().build())
                            .hashtags(List.of())
                            .mediaAttachments(List.of())
                            .edited(false)
                            .topic(-1L)
                            .internalPostId(posting.getId().toString())
                            .build();
                }).toList();
        ungroupedPostCollection.getPosts().addAll(recentPostings);

        TopicsAndPostCollectionDto topicsAndPostCollection = this.recognitionServiceClient.getGroupedAndLabeledPosts(
                RecognitionServiceRequest.builder()
                        .postCollection(ungroupedPostCollection)
                        .threshold(0.2f)
                        .build()
        );
        this.handleGroupedPostCollection(topicsAndPostCollection);
    }

    @Transactional
    @Override
    public void handleGroupedPostCollection(TopicsAndPostCollectionDto topicsAndPostCollectionDto) {
        Platform platform = this.createOrGetPlatform(topicsAndPostCollectionDto.getOutputPostCollection());

        int newPosts = 0;

        logger.debug(String.format("Creating %d topics...", topicsAndPostCollectionDto.getTopics().size()));
        for (TopicDto topicDto : topicsAndPostCollectionDto.getTopics()) {
            createTopic(topicDto);
        }

        for (List<PostDto> postGroup : topicsAndPostCollectionDto.getOutputPostCollection().getPosts()) {
            List<Source> newSourcesForGroup = this.saveUnknownSources(postGroup);
            PostDto repPost = this.getRepresentativePost(postGroup);

            if (repPost == null) {
                logger.warn("Could not determine representative post for group");
                continue;
            }

            Posting posting = null;
            if (repPost.getInternalPostId() != null && !repPost.getInternalPostId().isEmpty()) {
                posting = this.postingRepository.findById(UUID.fromString(repPost.getInternalPostId())).get();
            } else {
                Optional<Topic> topic = this.topicRepository.findById(repPost.getTopic());
                if (topic.isPresent()) {
                    posting = postingRepository.save(
                            Posting.builder()
                                    .summary(repPost.getContent())
                                    .topic(topic.get())
                                    .originatesFrom(platform)
                                    .published(repPost
                                            .getCreatedAt()
                                            .toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime())
                                    .build()
                    );
                    newPosts++;
                } else {
                    logger.error(String.format("Failed to find topic %d assigned to post", repPost.getTopic()));
                }
            }

            if (posting != null) {
                if (posting.getSources() != null) {
                    posting.getSources().addAll(newSourcesForGroup);
                } else {
                    posting.setSources(newSourcesForGroup);
                }
                this.postingRepository.save(posting);
            }
        }

        logger.info(String.format("Created %d new posts from latest scrape", newPosts));
    }

    private Platform createOrGetPlatform(GroupedPostCollectionDto groupedPostCollection) {
        Platform platform;
        // Save platform if it's not yet known
        if (!platformRepository.existsByName(groupedPostCollection.getSource().getSourceType())) {
            platform = platformRepository.save(
                    Platform.builder()
                            .name(groupedPostCollection.getSource().getSourceType())
                            .build()
            );
        } else {
            platform = platformRepository.findByName(groupedPostCollection.getSource().getSourceType());
        }
        return platform;
    }

    private void createTopic(TopicDto topicDto) {
        logger.debug(
                String.format("Creating topic %s with keywords %s",
                        topicDto.getLabel(),
                        String.join(",", topicDto.getKeywords())
                )
        );
        String topicText = topicDto.getLabel();
        topicRepository.save(
                Topic.builder()
                        .id(topicDto.getTopicId())
                        .name(topicText)
                        .keywords(topicDto.getKeywords())
                        .build()
        );
    }

    private List<Source> saveUnknownSources(List<PostDto> postGroup) {
        List<Source> newSourcesForGroup = new ArrayList<>();
        for (PostDto post : postGroup) {
            if (post.getUrl() == null || post.getUrl().isEmpty()) {
                continue;
            }

            URL sourceUrl;
            try {
                sourceUrl = new URL(post.getUrl());
            } catch (MalformedURLException e) {
                logger.warn(String.format("Post %s source is an invalid URL: %s\n", post.getContent().substring(0, 20), post.getUrl()));
                continue;
            }
            if (!this.sourceRepository.existsByLocation(sourceUrl)) {
                newSourcesForGroup.add(Source.builder()
                        .location(sourceUrl)
                        .build()
                );
            }
        }
        return this.sourceRepository.saveAll(newSourcesForGroup);
    }

    /**
     * Determines a representative post for the group, which is either a post
     * we previously persisted or the first post of the group.
     *
     * @param postGroup The post group containing similar/identical posts
     * @return the representative post for the group
     */
    private PostDto getRepresentativePost(List<PostDto> postGroup) {
        if (postGroup == null) {
            return null;
        }

        // Select post to save
        PostDto repPost = null;
        for (PostDto post : postGroup) {
            // If we find a post that we persisted before, we use it as the
            // representative post.
            if (post.getInternalPostId() != null) {
                repPost = post;
            }
        }
        // If no previously persisted post could be found, use the first
        // of the group.
        if (repPost == null) {
            repPost = postGroup.get(0);
        }
        return repPost;
    }

    @Override
    public Commentary addCommentToPost(String id, CommentaryDto commentaryDto) {

        Posting posting = getPost(id);

        Commentary commentary = new Commentary();
        commentary.setRelatedPost(posting);
        commentary.setScore(0);
        commentary.setVersion(1);
        commentary.setText(commentaryDto.getText());
        commentary.setPublished(LocalDateTime.now());

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = this.userService.getUserIdByEmail(principal);
        Journalist journalist = this.journalistService.getJournalistById(UUID.fromString(userId));

        commentary.setPublishedBy(journalist);

        return commentaryRepository.save(commentary);
    }

    @Override
    public Posting getPost(String id) {
        Optional<Posting> posting = this.postingRepository.findById(UUID.fromString(id));
        if (posting.isPresent()) {
            return posting.get();
        }

        throw new EntityNotFoundException("No Post with id [" + id + "]");
    }

    @Override
    public Commentary getComment(String id) {
        Optional<Commentary> commentary = this.commentaryRepository.findById(UUID.fromString(id));
        if (commentary.isPresent()) {
            return commentary.get();
        }

        throw new EntityNotFoundException("No Commentary with id [" + id + "]");
    }

    @Override
    public List<Commentary> getComments(String id) {
        Posting posting = getPost(id);
        List<Commentary> commentaries = this.commentaryRepository.findAllByRelatedPost(posting);

        return commentaries;
    }

    @Override
    public void deleteComment(String commentId) {
        this.commentaryRepository.deleteById(UUID.fromString(commentId));
    }

    @Override
    public void upvote(String commentId) {
        unvote(commentId);

        Commentary commentary = getComment(commentId);
        List<VerifeedUser> upvoters = commentary.getUpvotedBy();

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VerifeedUser user = this.userService.getUserByEmail(principal).get();

        upvoters.add(user);

        commentary.setUpvotedBy(upvoters);
        commentary.setScore(commentary.getScore() + 1);
        commentaryRepository.save(commentary);
    }

    @Override
    public void downvote(String commentId) {
        unvote(commentId);

        Commentary commentary = getComment(commentId);
        List<VerifeedUser> downvoters = commentary.getDownvotedBy();

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VerifeedUser user = this.userService.getUserByEmail(principal).get();

        downvoters.add(user);

        commentary.setDownvotedBy(downvoters);
        commentary.setScore(commentary.getScore() - 1);
        commentaryRepository.save(commentary);
    }

    @Override
    public void unvote(String commentId) {
        Commentary commentary = getComment(commentId);
        List<VerifeedUser> upvoters = commentary.getUpvotedBy();

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VerifeedUser user = this.userService.getUserByEmail(principal).get();

        boolean hasUpvoted = upvoters.stream()
                .anyMatch(upvoter -> upvoter.getId().equals(user.getId()));

        if (hasUpvoted) {
            upvoters.removeIf(upvoter -> upvoter.getId().equals(user.getId()));
            commentary.setUpvotedBy(upvoters);
            commentary.setScore(commentary.getScore() - 1);

            commentaryRepository.save(commentary);
        }

        List<VerifeedUser> downvoters = commentary.getDownvotedBy();

        boolean hasDownvoted = downvoters.stream()
                .anyMatch(downvoter -> downvoter.getId().equals(user.getId()));

        if (hasDownvoted) {
            downvoters.removeIf(downvoter -> downvoter.getId().equals(user.getId()));
            commentary.setDownvotedBy(downvoters);
            commentary.setScore(commentary.getScore() + 1);

            commentaryRepository.save(commentary);
        }
    }

    @Override
    public void editCommentary(String commentId, CommentaryDto commentaryDto) {

        Commentary commentary = getComment(commentId);
        commentary.setText(commentaryDto.getText());
        commentary.setVersion(commentary.getVersion()+1);

        commentaryRepository.save(commentary);
    }

    @Override
    public List<Posting> getPosts(Long offset, Long limit, Boolean onlyCommented, String q) {
        q = Optional.ofNullable(q).orElse("").trim();

        // Check if the searchString is a URL and search for the url in the DB.
        // If not found in the DB trigger the scraping service with the given url
        Optional<URL> postURL = tryCreateURL(q);
        if (postURL.isPresent() && !blacklistRepository.isBlacklisted(postURL.get().toString())) {
            List<Source> sources = sourceRepository.findByLocation(postURL.get());

            if (sources.isEmpty()) {
                CompletableFuture<Void> future = initiateScrapingURL(postURL.get().toString());
                try {
                    future.get(); // Wait until the pipeline has hopefully finished and the post is in the DB
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Error while waiting for scraped post to be in the DB!", e);
                }
            } else {
                return postingRepository.getPosts(offset, limit, onlyCommented, postURL.get().toString());
            }

        }

        return postingRepository.getPosts(offset, limit, onlyCommented, q);
    }

    @Override
    public void blockPost(String id) {
        Posting posting = getPost(id);
        posting.getSources().forEach(this::addSourceToBlacklist);

        Topic topic = posting.getTopic();
        Platform platform = posting.getOriginatesFrom();

        posting.setOriginatesFrom(null);
        posting.setTopic(null);
        postingRepository.save(posting);

        topic.getRelatedPosts().remove(posting);
        topicRepository.save(topic);

        platform.getPostings().remove(posting);
        platformRepository.save(platform);

        postingRepository.delete(posting);
    }

    @Override
    public void addSourceToBlacklist(Source source) {
        Blacklist blacklist = new Blacklist();
        blacklist.setLocation(source.getLocation());
        blacklistRepository.save(blacklist);
    }


    /**
     * Tries to create a core URL from the given string by removing query parameters and fragments.
     *
     * @param urlString the string to create the core URL from
     * @return the core URL if the string is a valid URL, otherwise an empty optional
     */
    private Optional<URL> tryCreateURL(String urlString) {
        try {
            urlString = URLDecoder.decode(urlString, StandardCharsets.UTF_8);
            URL url = new URL((urlString == null) ? "" : urlString);

            String coreUrlString = url.getProtocol() + "://" + url.getHost() + url.getPath();

            return Optional.of(new URL(coreUrlString));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }


    /**
     * Initiates the scraping of the given URL.
     * This method will return immediately and the scraping will be done in a separate thread.
     * @see ScrapingServiceClient
     * @param url the URL to scrape
     * @return a CompletableFuture that will be completed when the scraping is done
     */
    private CompletableFuture<Void> initiateScrapingURL(String url) {
        logger.info("Sending request to the scraping service to scrape URL: " + url);
        ScrapingServiceClient.ScrapeUrlRequest urlRequest = new ScrapingServiceClient.ScrapeUrlRequest();
        urlRequest.setUrl(url);

        // Start the scraping operation in a separate thread
        return CompletableFuture.runAsync(() -> {
        scrapingServiceClient.scrapePostingWithUrl(urlRequest);
        });
    }

}
