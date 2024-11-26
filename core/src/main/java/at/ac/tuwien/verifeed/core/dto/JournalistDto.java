package at.ac.tuwien.verifeed.core.dto;

import at.ac.tuwien.verifeed.core.entities.Address;
import at.ac.tuwien.verifeed.core.entities.JournalistVerificationDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JournalistDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private Integer score;

    private boolean verified;

    private JournalistAffiliationDto adminOf;

    private List<JournalistAffiliationDto> publishesFor;

    private Address address;

    private JournalistVerificationDetails journalistVerificationDetails;

    private String img;

}
