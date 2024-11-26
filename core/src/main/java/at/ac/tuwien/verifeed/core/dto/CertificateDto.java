package at.ac.tuwien.verifeed.core.dto;

import at.ac.tuwien.verifeed.core.entities.VerificationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CertificateDto {

    private UUID id;

    private URL reference;

    private String explanation;

    private CertificateJournalistDto certificateHolder;

    private List<CertificateJournalistDto> verifiedJournalists;

    private List<VerificationRequest> verificationRequests;

}
