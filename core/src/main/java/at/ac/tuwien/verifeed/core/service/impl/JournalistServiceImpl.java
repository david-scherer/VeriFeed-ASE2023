package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.mapper.impl.JournalistMapperImpl;
import at.ac.tuwien.verifeed.core.repositories.JournalistRepository;
import at.ac.tuwien.verifeed.core.service.JournalistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class JournalistServiceImpl implements JournalistService {
    Logger logger = LoggerFactory.getLogger(JournalistServiceImpl.class);
    private final JournalistRepository journalistRepository;
    private final JournalistMapperImpl journalistMapper;

    @Autowired
    public JournalistServiceImpl(JournalistRepository journalistRepository, JournalistMapperImpl journalistMapper) {
        this.journalistRepository = journalistRepository;
        this.journalistMapper = journalistMapper;
    }

    @Override
    public List<Journalist> getVerifiedJournalists() {
        List<Journalist> journalistList = journalistRepository.findAll();
        journalistList.removeIf(journalist -> !journalist.isVerified());
        return journalistList;
    }

    @Override
    public List<Journalist> getUnverifiedJournalists() {
        List<Journalist> journalistList = journalistRepository.findAll();
        journalistList.removeIf(Journalist::isVerified);
        return journalistList;
    }

    @Override
    public Journalist getJournalistById(UUID uuid) throws EntityNotFoundException {
        Optional<Journalist> journalist = journalistRepository.findById(uuid);
        if (journalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist with ID " + uuid + "not found.");
        }
        return journalist.get();
    }

    @Override
    public List<Journalist> getVerifiedCertifiedJournalists() {
        List<Journalist> journalistList = journalistRepository.findAll();
        journalistList.removeIf(journalist -> !journalist.isVerified() && journalist.getHolderOfCertificate() == null);
        return journalistList;
    }

    @Override
    public List<Journalist> getVerifiedUncertifiedJournalists() {
        List<Journalist> journalistList = journalistRepository.findAll();
        journalistList.removeIf(journalist -> !journalist.isVerified() && journalist.getHolderOfCertificate() != null);
        return journalistList;
    }

    @Override
    public void verifyJournalist(UUID uuid) throws EntityNotFoundException {
        Optional<Journalist> journalist = journalistRepository.findById(uuid);
        if (journalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist with ID " + uuid + "not found.");
        }
        journalist.get().setVerified(true);
        journalistRepository.save(journalist.get());
    }

    @Override
    public void unverifyJournalist(UUID uuid) throws EntityNotFoundException {
        Optional<Journalist> journalist = journalistRepository.findById(uuid);
        if (journalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist with ID " + uuid + "not found.");
        }
        journalist.get().setVerified(false);
        journalistRepository.save(journalist.get());
    }


}
