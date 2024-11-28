package at.ac.tuwien.verifeed.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VerifeedUserDto {

    private UUID id;

    private String username;

    private String email;

    private boolean locked;

    private boolean enabled;

    private boolean adminOf;

    private String img;

}
