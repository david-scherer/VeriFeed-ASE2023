package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Id
    private Long id;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Posting> relatedPosts;

    @ElementCollection
    private List<String> keywords;
}
