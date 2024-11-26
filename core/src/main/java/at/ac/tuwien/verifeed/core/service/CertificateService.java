package at.ac.tuwien.verifeed.core.service;

import at.ac.tuwien.verifeed.core.dto.CertificateDto;
import at.ac.tuwien.verifeed.core.dto.CertificateRequest;
import at.ac.tuwien.verifeed.core.entities.Certificate;
import at.ac.tuwien.verifeed.core.entities.Journalist;

import java.net.URL;
import java.util.List;
import java.util.UUID;

public interface CertificateService {
    List<Certificate> getCertificates();

    void createCertificate(Journalist journalist, String explanation, URL reference);

    void createCertificate(CertificateRequest certificateRequest);

    void revokeCertificate(UUID uuid);
}
