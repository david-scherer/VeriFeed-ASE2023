package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Boolean existsByName(String name);

    Platform findByName(String name);
}
