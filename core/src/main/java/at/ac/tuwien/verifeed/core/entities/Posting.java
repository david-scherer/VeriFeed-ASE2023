package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column
    private LocalDateTime published;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Topic topic;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Platform originatesFrom;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "related_post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Commentary> commentaries;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Source> sources;
}
