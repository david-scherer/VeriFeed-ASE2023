package at.ac.tuwien.verifeed.core.entities;

import at.ac.tuwien.verifeed.core.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class VerifeedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Column(nullable = false)
    private boolean locked = false;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(columnDefinition = "TEXT")
    private String img;

    @OneToMany
    @JoinColumn
    private List<VerificationRequest> verificationRequests;

    @OneToMany
    @JoinColumn
    private List<ConfirmationToken> confirmationTokens;

    public VerifeedUser(String username, String email, String password, UserRole role) {
        this.userRole = role;
        this.password = password;
        this.username = username;
        this.email = email;
    }
}


