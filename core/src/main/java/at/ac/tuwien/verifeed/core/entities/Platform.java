package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column
    private String serviceId;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Posting> postings;
}
