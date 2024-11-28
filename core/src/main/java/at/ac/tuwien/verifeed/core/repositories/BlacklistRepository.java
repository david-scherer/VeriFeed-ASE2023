package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, UUID> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Blacklist b WHERE CAST(b.location AS text) = :url")
    boolean isBlacklisted(String url);
}
