package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.net.URL;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JournalistVerificationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Journalist journalist;

    @Column
    private String employer;

    @Column
    private Integer distributionReach;

    @Column
    private String mainMedium;

    @Column
    private URL reference;

    @Column(length = 2056)
    private String message;
}
