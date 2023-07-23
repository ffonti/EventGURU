package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "normal_user")
@Table(name = "normal_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
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
