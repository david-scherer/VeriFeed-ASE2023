package at.ac.tuwien.verifeed.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EditUserDto {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
