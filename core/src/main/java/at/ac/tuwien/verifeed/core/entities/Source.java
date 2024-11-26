package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.*;
import lombok.*;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private URL location;

    @ManyToMany
    private List<Commentary> referencedByCommentaries;

    @ManyToMany
    private List<Posting> referencedByPostings;
}
