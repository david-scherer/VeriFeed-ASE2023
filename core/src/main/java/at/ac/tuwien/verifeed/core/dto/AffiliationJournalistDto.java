package at.ac.tuwien.verifeed.core.dto;

import at.ac.tuwien.verifeed.core.entities.JournalistVerificationDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AffiliationJournalistDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private Integer score;

    private boolean verified;

    private JournalistVerificationDetails journalistVerificationDetails;

}
