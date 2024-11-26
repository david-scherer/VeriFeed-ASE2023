package at.ac.tuwien.verifeed.core.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegistrationRequest {
    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
