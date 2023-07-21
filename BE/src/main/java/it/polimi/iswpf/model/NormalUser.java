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

    public NormalUser(String email, String password, String username, Ruolo ruolo, String nome, String cognome) {
        super(email, password, username, ruolo);
        this.nome = nome;
        this.cognome = cognome;
    }
}
