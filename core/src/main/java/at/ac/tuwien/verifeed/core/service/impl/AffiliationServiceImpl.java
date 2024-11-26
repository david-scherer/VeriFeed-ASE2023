package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.dto.AffiliationRequest;
import at.ac.tuwien.verifeed.core.entities.Affiliation;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.exception.MissingPermissionException;
import at.ac.tuwien.verifeed.core.mapper.impl.AffiliationMapperImpl;
import at.ac.tuwien.verifeed.core.repositories.AffiliationRepository;
import at.ac.tuwien.verifeed.core.repositories.JournalistRepository;
import at.ac.tuwien.verifeed.core.service.AffiliationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AffiliationServiceImpl implements AffiliationService {
    Logger logger = LoggerFactory.getLogger(AffiliationServiceImpl.class);

    private final AffiliationRepository affiliationRepository;
    private final JournalistRepository journalistRepository;
    private final AffiliationMapperImpl affiliationMapper;

    @Autowired
    public AffiliationServiceImpl(AffiliationRepository affiliationRepository, JournalistRepository journalistRepository, AffiliationMapperImpl affiliationMapper) {
        this.affiliationRepository = affiliationRepository;
        this.journalistRepository = journalistRepository;
        this.affiliationMapper = affiliationMapper;
    }

    @Override
    public void createAffiliation(AffiliationRequest request) {
        Optional<Journalist> journalist = journalistRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (journalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist not found.");
        }
        Affiliation affiliation = new Affiliation();
        affiliation.setName(request.getName());
        affiliation.setVerified(false);
        affiliation.setAddress(request.getAddress());
        affiliation.setOwner(journalist.get());
        affiliation = affiliationRepository.save(affiliation);
        logger.info(affiliation.getId() + " " + affiliation.getName() + " created");
    }

    @Override
    public void verifyAffiliation(UUID uuid) {
        if (affiliationRepository.findById(uuid).isEmpty()) {
            throw new EntityNotFoundException("Affiliation with ID " + uuid + "not found.");
        }
        Affiliation affiliation = affiliationRepository.findById(uuid).get();
        affiliation.setVerified(true);
        affiliationRepository.save(affiliation);
    }

    @Override
    public boolean belongsJournalistToAffiliation(UUID uuidJournalist) {
        Optional<Journalist> owner = journalistRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (owner.isEmpty()) {
            throw new EntityNotFoundException("Owner not found.");
        }
        Optional<Affiliation> affiliation = affiliationRepository.findByOwner(owner.get());
        if (affiliation.isEmpty()) {
            throw new EntityNotFoundException("You are not administering any affiliation.");
        }
        List<Journalist> journalists = affiliation.get().getJournalists();
        for (Journalist journalist : journalists) {
            if (journalist.getId().equals(uuidJournalist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addJournalistToAffiliation(UUID uuidJournalist) {
        Optional<Journalist> owner = journalistRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (owner.isEmpty()) {
            throw new EntityNotFoundException("Journalist not found.");
        }
        Optional<Affiliation> affiliation = affiliationRepository.findByOwner(owner.get());
        if (affiliation.isEmpty()) {
            throw new EntityNotFoundException("You are not administering any affiliation.");
        }
        if (journalistRepository.findById(uuidJournalist).isEmpty()) {
            throw new EntityNotFoundException("Journalist with ID " + uuidJournalist + "not found.");
        }
        Journalist journalist = journalistRepository.findById(uuidJournalist).get();
        if (!journalist.isVerified()) {
            throw new MissingPermissionException("Journalist is not verified");
        }
        affiliation.get().getJournalists().add(journalist);
        affiliationRepository.save(affiliation.get());
    }

    @Override
    public void removeJournalistFromAffiliation(UUID uuidJournalist) {
        Optional<Journalist> owner = journalistRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (owner.isEmpty()) {
            throw new EntityNotFoundException("Journalist not found.");
        }
        Optional<Affiliation> affiliation = affiliationRepository.findByOwner(owner.get());
        if (affiliation.isEmpty()) {
            throw new EntityNotFoundException("You are not administering any affiliation.");
        }
        List<Journalist> journalists = affiliation.get().getJournalists();
        if(!journalists.removeIf(journalist -> journalist.getId().equals(uuidJournalist))) {
            throw new EntityNotFoundException("Journalist with ID " + uuidJournalist + " not a member.");
        }
        Affiliation result = affiliation.get();
        result.setJournalists(journalists);
        affiliationRepository.save(result);
    }

    @Override
    public List<Affiliation> getVerifiedAffiliations() {
        List<Affiliation> affiliationList = affiliationRepository.findAll();
        affiliationList.removeIf(affiliation -> !affiliation.isVerified());
        return affiliationList;
    }

    @Override
    public List<Affiliation> getUnverifiedAffiliations() {
        List<Affiliation> affiliationList = affiliationRepository.findAll();
        affiliationList.removeIf(Affiliation::isVerified);
        return affiliationList;
    }

    @Override
    public Affiliation getAffiliationById(UUID uuid) {
        Optional<Affiliation> affiliation = affiliationRepository.findById(uuid);
        if (affiliation.isEmpty()) {
            throw new EntityNotFoundException("Affiliation with ID " + uuid + "not found.");
        }
        return affiliation.get();
    }
}
