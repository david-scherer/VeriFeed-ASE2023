package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Commentary;
import at.ac.tuwien.verifeed.core.entities.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, UUID> {

    List<Commentary> findAllByRelatedPost(Posting posting);
}
