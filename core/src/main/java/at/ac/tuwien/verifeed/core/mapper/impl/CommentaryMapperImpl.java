package at.ac.tuwien.verifeed.core.mapper.impl;

import at.ac.tuwien.verifeed.core.dto.CommentaryDto;
import at.ac.tuwien.verifeed.core.entities.Commentary;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.mapper.CommentaryMapper;
import at.ac.tuwien.verifeed.core.mapper.JournalistMapper;
import at.ac.tuwien.verifeed.core.mapper.PostingMapper;
import at.ac.tuwien.verifeed.core.mapper.SourceMapper;
import at.ac.tuwien.verifeed.core.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentaryMapperImpl implements CommentaryMapper {

    private final UserService userService;

    private final JournalistMapper journalistMapper;

    private final SourceMapper sourceMapper;

    private final PostingMapper postingMapper;

    public CommentaryMapperImpl(UserService userService, JournalistMapper journalistMapper, SourceMapper sourceMapper, PostingMapper postingMapper) {
        this.userService = userService;
        this.journalistMapper = journalistMapper;
        this.sourceMapper = sourceMapper;
        this.postingMapper = postingMapper;
    }

    @Override
    public CommentaryDto entityToDto(Commentary commentary) {

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<VerifeedUser> verifeedUserOptional = this.userService.getUserByEmail(principal);


        CommentaryDto commentaryDto = new CommentaryDto();
        commentaryDto.setId(commentary.getId());
        commentaryDto.setScore(commentary.getScore());
        commentaryDto.setText(commentary.getText());
        commentaryDto.setRelatedPost(postingMapper.entityToDto(commentary.getRelatedPost()));
        commentaryDto.setPublished(commentary.getPublished());
        commentaryDto.setVersion(commentary.getVersion());
        commentaryDto.setPublishedBy(journalistMapper.entityToDto(commentary.getPublishedBy()));

        List<VerifeedUser> upvoters = commentary.getUpvotedBy();

        if (verifeedUserOptional.isPresent()) {
            VerifeedUser user = verifeedUserOptional.get();

            boolean hasUpvoted = false;
            if (upvoters != null) {
                hasUpvoted = upvoters.stream()
                        .anyMatch(upvoter -> upvoter.getId().equals(user.getId()));
            }

            List<VerifeedUser> downvoters = commentary.getDownvotedBy();

            boolean hasDownvoted = false;
            if (downvoters != null) {
                hasDownvoted = downvoters.stream()
                        .anyMatch(downvoter -> downvoter.getId().equals(user.getId()));
            }

            if (hasUpvoted) {
                commentaryDto.setVote(1);
            } else if (hasDownvoted) {
                commentaryDto.setVote(-1);
            } else {
                commentaryDto.setVote(0);
            }
        } else {
            commentaryDto.setVote(0);
        }

        return commentaryDto;
    }
}
