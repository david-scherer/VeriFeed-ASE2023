package at.ac.tuwien.verifeed.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JournalistAffiliationDto {

    private UUID id;

    private String name;

    private URL address;

    private boolean verified;
}
