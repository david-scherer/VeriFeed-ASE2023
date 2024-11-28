package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostingRepository extends JpaRepository<Posting, UUID> {

    @Query(value = "SELECT p.* FROM posting p " +
            "LEFT JOIN commentary c ON p.id = c.related_post_id " +
            "LEFT JOIN topic t ON p.topic_id = t.id " +
            "LEFT JOIN journalist j ON c.published_by_id = j.id " +
            "LEFT JOIN affiliation a ON j.id = a.id " +
            "WHERE (:onlyCommented = false OR c.id IS NOT NULL) " +
            "AND (LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.summary) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR EXISTS (SELECT 1 FROM posting_sources ps JOIN source s ON ps.sources_id = s.id WHERE ps.posting_id = p.id AND LOWER(s.location) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "OR LOWER(c.text) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(j.first_name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(j.last_name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(CONCAT(j.first_name, ' ', j.last_name)) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(CONCAT(j.last_name, ' ', j.first_name)) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(CONCAT(j.last_name, ', ', j.first_name)) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(a.name) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "GROUP BY p.id " +
            "ORDER BY p.published DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Posting> getPosts(Long offset, Long limit, Boolean onlyCommented, String q);

    Optional<Posting> findById(UUID uuid);

    @Query("select p from Posting p where p.published >= :date")
    List<Posting> findByPublishedAfter(LocalDateTime date);

}
