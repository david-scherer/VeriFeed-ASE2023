package at.ac.tuwien.verifeed.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AffiliationDto {

    private UUID id;

    private String name;

    private URL address;

    private boolean verified;

    private AffiliationJournalistDto owner;

    private List<AffiliationJournalistDto> journalists;

}
