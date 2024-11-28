package at.ac.tuwien.verifeed.core.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private URL reference;

    @Column
    private String explanation;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Journalist certificateHolder;

    @OneToMany
    private List<Journalist> verifiedJournalists;

    @OneToMany
    @JoinColumn
    private List<VerificationRequest> verificationRequests;
}
