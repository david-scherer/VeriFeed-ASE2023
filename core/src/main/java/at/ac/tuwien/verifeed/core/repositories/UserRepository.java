package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<VerifeedUser, UUID> {

    Optional<VerifeedUser> findByUsername(String username);

    Optional<VerifeedUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE VerifeedUser u SET u.enabled = TRUE WHERE u.email = ?1")
    int enableUserByEMail(String email);

}
