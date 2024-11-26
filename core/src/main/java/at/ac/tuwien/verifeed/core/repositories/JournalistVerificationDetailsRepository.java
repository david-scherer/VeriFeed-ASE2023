package at.ac.tuwien.verifeed.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JournalistVerificationDetailsRepository extends JpaRepository<at.ac.tuwien.verifeed.core.entities.JournalistVerificationDetails, UUID> {
}
