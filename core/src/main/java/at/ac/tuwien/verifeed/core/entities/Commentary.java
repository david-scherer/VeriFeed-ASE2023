package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commentary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Integer version;

    @Column
    private Integer score;

    @ManyToMany
    private List<VerifeedUser> upvotedBy;

    @ManyToMany
    private List<VerifeedUser> downvotedBy;

    @Column
    private LocalDateTime published;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Journalist publishedBy;

    @ManyToOne
    private Posting relatedPost;

    @ManyToMany
    private List<Source> sources;
}
