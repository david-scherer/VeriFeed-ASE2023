package at.ac.tuwien.verifeed.core.dto;

import at.ac.tuwien.verifeed.core.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VerificationRequestAnswerDto {

    private UUID id;

    private VerifeedUserDto requester;

    private CertificateDto usedCertificate;

    private Boolean status;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private Address address;

    private String employer;

    private Integer distributionReach;

    private String mainMedium;

    private URL reference;

    private String requestMessage;
}
