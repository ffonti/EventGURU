package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity(name = "NormalUser")
@Table(name = "normal_user")
public class NormalUser extends User {

    @Column(name = "nome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Column(name = "cognome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String cognome;
}
