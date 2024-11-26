package at.ac.tuwien.verifeed.core.repositories;

import at.ac.tuwien.verifeed.core.entities.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.List;
import java.util.UUID;


@Repository
public interface SourceRepository extends JpaRepository<Source, UUID> {

    Boolean existsByLocation(URL location);

    List<Source> findByLocation(URL location);

}
