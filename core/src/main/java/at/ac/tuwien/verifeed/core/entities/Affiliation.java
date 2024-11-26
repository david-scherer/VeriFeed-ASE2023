package at.ac.tuwien.verifeed.core.entities;

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
public class Affiliation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private URL address;

    @Column
    private boolean verified;

    @OneToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Journalist owner;

    @ManyToMany
    @JoinTable(name = "journalist_publishes_for_affiliation")
    private List<Journalist> journalists;
}
