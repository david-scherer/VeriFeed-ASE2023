package at.ac.tuwien.verifeed.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentials {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoginCredentials userLoginModel)) {
            return false;
        }
        return Objects.equals(email, userLoginModel.email)
                && Objects.equals(password, userLoginModel.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "LoginCredentials{"
                + "email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
