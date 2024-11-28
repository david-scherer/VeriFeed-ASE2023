package at.ac.tuwien.verifeed.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AffiliationRequest {

    @NotNull
    private String name;

    private URL address;

}
