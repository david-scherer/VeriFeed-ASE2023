package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Journalist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JournalistRepository extends JpaRepository<Journalist, UUID> {

    Optional<Journalist> findByEmail(String email);

}
