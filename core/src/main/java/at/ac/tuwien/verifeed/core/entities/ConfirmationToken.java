package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String token;

    @Column
    private LocalDateTime creationDate;

    @Column
    private LocalDateTime confirmationDate;

    @Column
    private LocalDateTime expirationDate;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VerifeedUser user;

    public ConfirmationToken(String token, LocalDateTime creationDate, LocalDateTime expirationDate, VerifeedUser user) {
        this.token = token;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.user = user;
    }
}
