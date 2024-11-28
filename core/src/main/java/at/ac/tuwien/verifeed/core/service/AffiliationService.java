package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.AffiliationDto;
import at.ac.tuwien.verifeed.core.dto.AffiliationRequest;
import at.ac.tuwien.verifeed.core.entities.Affiliation;

import java.util.List;
import java.util.UUID;

public interface AffiliationService {
    void createAffiliation(AffiliationRequest request);

    void verifyAffiliation(UUID uuid);

    void addJournalistToAffiliation(UUID uuidJournalist);

    void removeJournalistFromAffiliation(UUID uuidJournalist);

    List<Affiliation> getVerifiedAffiliations();

    List<Affiliation> getUnverifiedAffiliations();

    Affiliation getAffiliationById(UUID uuid);

    boolean belongsJournalistToAffiliation(UUID uuidJournalist);
}
