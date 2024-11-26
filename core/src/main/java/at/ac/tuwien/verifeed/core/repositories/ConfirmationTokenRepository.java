package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.ConfirmationToken;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@Transactional
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    ConfirmationToken findByToken(String token);

    void deleteByUser(VerifeedUser user);


    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmationDate = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmationDate(String token,
                          LocalDateTime confirmationDate);
}
