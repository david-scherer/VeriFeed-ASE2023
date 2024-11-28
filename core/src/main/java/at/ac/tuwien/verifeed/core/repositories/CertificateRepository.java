package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
}
