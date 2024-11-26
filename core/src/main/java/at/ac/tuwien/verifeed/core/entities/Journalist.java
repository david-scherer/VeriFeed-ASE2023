package at.ac.tuwien.verifeed.core.entities;

import at.ac.tuwien.verifeed.core.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Journalist extends VerifeedUser {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.JOURNALIST;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Integer score = 0;

    @Column(nullable = false)
    private boolean verified = false;

    @OneToOne(mappedBy = "owner")
    private Affiliation adminOf;

    @ManyToMany(mappedBy = "journalists")
    private List<Affiliation> publishesFor;

    @OneToMany
    @JoinColumn(name = "journalist_commentaries")
    private List<Commentary> commentaries;

    @OneToOne
    @JoinColumn(name = "journalists_holds_certificate")
    private Certificate holderOfCertificate;

    @ManyToOne
    @JoinColumn(name = "journalist_verified_by")
    private Certificate verifiedBy;

    @ManyToOne
    @JoinColumn(name = "journalist_address")
    private Address address;

    @OneToOne
    @JoinColumn(name = "journalist_verification_details")
    private JournalistVerificationDetails journalistVerificationDetails;

}
