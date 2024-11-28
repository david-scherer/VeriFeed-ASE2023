package at.ac.tuwien.verifeed.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String country;

    @Column
    private String state;

    @Column
    private String city;

    @Column
    private String postalCode;

    @Column
    private String street;
}
