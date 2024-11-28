package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Affiliation;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AffiliationRepository extends JpaRepository<Affiliation, UUID> {

    Optional<Affiliation> findByOwner(Journalist owner);
}
