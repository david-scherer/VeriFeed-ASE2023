package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.JournalistDto;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface JournalistService {
    List<Journalist> getVerifiedJournalists();

    List<Journalist> getUnverifiedJournalists();

    Journalist getJournalistById(UUID uuid) throws EntityNotFoundException;

    List<Journalist> getVerifiedCertifiedJournalists();

    List<Journalist> getVerifiedUncertifiedJournalists();

    void verifyJournalist(UUID uuid) throws EntityNotFoundException;

    void unverifyJournalist(UUID uuid) throws EntityNotFoundException;
}
