package at.ac.tuwien.verifeed.core.service.impl;

import at.ac.tuwien.verifeed.core.dto.VerificationRequestDto;
import at.ac.tuwien.verifeed.core.entities.Address;
import at.ac.tuwien.verifeed.core.entities.Certificate;
import at.ac.tuwien.verifeed.core.entities.Journalist;
import at.ac.tuwien.verifeed.core.entities.JournalistVerificationDetails;
import at.ac.tuwien.verifeed.core.entities.VerifeedUser;
import at.ac.tuwien.verifeed.core.entities.VerificationRequest;
import at.ac.tuwien.verifeed.core.enums.UserRole;
import at.ac.tuwien.verifeed.core.exception.EntityNotFoundException;
import at.ac.tuwien.verifeed.core.exception.MissingPermissionException;
import at.ac.tuwien.verifeed.core.mapper.impl.VerificationRequestMapperImpl;
import at.ac.tuwien.verifeed.core.repositories.AddressRepository;
import at.ac.tuwien.verifeed.core.repositories.CertificateRepository;
import at.ac.tuwien.verifeed.core.repositories.JournalistRepository;
import at.ac.tuwien.verifeed.core.repositories.JournalistVerificationDetailsRepository;
import at.ac.tuwien.verifeed.core.repositories.UserRepository;
import at.ac.tuwien.verifeed.core.repositories.VerificationRepository;
import at.ac.tuwien.verifeed.core.service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements VerificationService {
    Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);
    private final VerificationRepository verificationRepository;
    private final JournalistRepository journalistRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final AddressRepository addressRepository;
    private final JournalistVerificationDetailsRepository journalistVerificationDetailsRepository;
    private final VerificationRequestMapperImpl verificationRequestMapper;

    @Autowired
    public VerificationServiceImpl(VerificationRepository verificationRepository, JournalistRepository journalistRepository,
                                   UserRepository userRepository,
                                   CertificateRepository certificateRepository,
                                   AddressRepository addressRepository,
                                   JournalistVerificationDetailsRepository journalistVerificationDetailsRepository,
                                    VerificationRequestMapperImpl verificationRequestMapper) {
        this.verificationRepository = verificationRepository;
        this.journalistRepository = journalistRepository;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.addressRepository = addressRepository;
        this.journalistVerificationDetailsRepository = journalistVerificationDetailsRepository;
        this.verificationRequestMapper = verificationRequestMapper;
    }

    @Override
    public List<VerificationRequest> getVerificationRequest() {
        Optional<Journalist> grantingJournalist = journalistRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (grantingJournalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist not found.");
        }
        List<VerificationRequest> verificationRequests = verificationRepository.findAll();
        verificationRequests.removeIf(verificationRequest -> !verificationRequest.getUsedCertificate().getCertificateHolder().equals(grantingJournalist.get()));
        return verificationRequests;
    }

    @Override
    public void createVerificationRequest(VerificationRequestDto verificationRequestDto) {
        VerificationRequest verificationRequest = new VerificationRequest();
        Optional<VerifeedUser> requester = userRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (requester.isEmpty()) {
            throw new EntityNotFoundException("User not found.");
        }
        Optional<Certificate> certificate = certificateRepository.findById(verificationRequestDto.getCertificateId());
        if (certificate.isEmpty()) {
            throw new EntityNotFoundException("Certificate not found.");
        }
        Address address = new Address();
        address.setCity(verificationRequestDto.getAddress().getCity());
        address.setCountry(verificationRequestDto.getAddress().getCountry());
        address.setState(verificationRequestDto.getAddress().getState());
        address.setPostalCode(verificationRequestDto.getAddress().getPostalCode());
        address.setStreet(verificationRequestDto.getAddress().getStreet());
        address = addressRepository.save(address);
        verificationRequest.setUsedCertificate(certificate.get());
        verificationRequest.setRequester(requester.get());
        verificationRequest.setFirstName(verificationRequestDto.getFirstName());
        verificationRequest.setLastName(verificationRequestDto.getLastName());
        verificationRequest.setDateOfBirth(verificationRequestDto.getDateOfBirth());
        verificationRequest.setAddress(address);
        verificationRequest.setEmployer(verificationRequestDto.getEmployer());
        verificationRequest.setDistributionReach(verificationRequestDto.getDistributionReach());
        verificationRequest.setMainMedium(verificationRequestDto.getMainMedium());
        verificationRequest.setReference(verificationRequestDto.getReference());
        verificationRequest.setRequestMessage(verificationRequestDto.getRequestMessage());
        verificationRepository.save(verificationRequest);
    }

    @Override
    public void grantVerificationRequest(UUID uuid) {
        Optional<VerificationRequest> verificationRequest = verificationRepository.findById(uuid);
        if (verificationRequest.isEmpty()) {
            throw new EntityNotFoundException("Verification request not found.");
        }
        Optional<Journalist> grantingJournalist = journalistRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (grantingJournalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist not found.");
        }
        if (!verificationRequest.get().getUsedCertificate().getCertificateHolder().equals(grantingJournalist.get())) {
            throw new MissingPermissionException("Granting journalist is not the correct certificate holder.");
        }
        Optional<Certificate> certificate = certificateRepository.findById(verificationRequest.get().getUsedCertificate().getId());
        if (certificate.isEmpty()) {
            throw new EntityNotFoundException("Certificate not found.");
        }
        verificationRequest.get().setStatus(true);
        verificationRepository.save(verificationRequest.get());
        VerifeedUser user = verificationRequest.get().getRequester();
        Journalist journalist = new Journalist();
        journalist.setUsername(user.getUsername());
        journalist.setEmail(user.getEmail());
        journalist.setUserRole(UserRole.JOURNALIST);
        journalist.setEnabled(user.isEnabled());
        journalist.setVerifiedBy(verificationRequest.get().getUsedCertificate());
        journalist.setPassword(user.getPassword());
        journalist.setFirstName(verificationRequest.get().getFirstName());
        journalist.setLastName(verificationRequest.get().getLastName());
        journalist.setDateOfBirth(verificationRequest.get().getDateOfBirth());
        journalist.setAddress(verificationRequest.get().getAddress());
        journalist.setVerified(true);
        JournalistVerificationDetails journalistVerificationDetails = new JournalistVerificationDetails();
        journalistVerificationDetails.setEmployer(verificationRequest.get().getEmployer());
        journalistVerificationDetails.setDistributionReach(verificationRequest.get().getDistributionReach());
        journalistVerificationDetails.setMainMedium(verificationRequest.get().getMainMedium());
        journalistVerificationDetails.setReference(verificationRequest.get().getReference());
        journalistVerificationDetails.setMessage(verificationRequest.get().getRequestMessage());
        journalistVerificationDetails = journalistVerificationDetailsRepository.save(journalistVerificationDetails);
        journalist.setJournalistVerificationDetails(journalistVerificationDetails);
        userRepository.delete(user);
        certificate.get().getVerifiedJournalists().add(journalistRepository.save(journalist));
        certificateRepository.save(certificate.get());
    }

    @Override
    public void declineVerificationRequest(UUID uuid) {
        Optional<VerificationRequest> verificationRequest = verificationRepository.findById(uuid);
        if (verificationRequest.isEmpty()) {
            throw new EntityNotFoundException("Verification request not found.");
        }
        Optional<Journalist> grantingJournalist = journalistRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (grantingJournalist.isEmpty()) {
            throw new EntityNotFoundException("Journalist not found.");
        }
        if (!verificationRequest.get().getUsedCertificate().getCertificateHolder().equals(grantingJournalist.get())) {
            throw new MissingPermissionException("Granting journalist is not the correct certificate holder.");
        }
        verificationRequest.get().setStatus(false);
        verificationRepository.save(verificationRequest.get());
    }
}
