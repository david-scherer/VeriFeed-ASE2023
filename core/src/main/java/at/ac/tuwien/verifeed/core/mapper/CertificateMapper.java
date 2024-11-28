package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.CertificateDto;
import at.ac.tuwien.verifeed.core.entities.Certificate;

public interface CertificateMapper {
    CertificateDto entityToDto(Certificate certificate);
}
