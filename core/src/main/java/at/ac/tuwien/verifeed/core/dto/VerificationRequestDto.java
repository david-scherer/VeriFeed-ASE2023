package at.ac.tuwien.verifeed.core.dto;

import at.ac.tuwien.verifeed.core.entities.Address;
import at.ac.tuwien.verifeed.core.entities.Certificate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class VerificationRequestDto {

    @NotNull
    private UUID certificateId;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    private Address address;

    private String employer;

    @NotNull
    private Integer distributionReach;

    @NotEmpty
    private String mainMedium;

    @NotNull
    private URL reference;

    @NotEmpty
    private String requestMessage;
}
