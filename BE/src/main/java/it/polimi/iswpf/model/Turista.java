package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.TuristaBuilder;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
@Entity(name = "Turista")
@Table(name = "turista")
public class Turista extends NormalUser {

    @Column(name = "iscritto_newsletter", nullable = false)
    private boolean iscrittoNewsletter;

    @OneToMany(mappedBy = "user")
    private List<Recensione> recensioni;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "segue",
            joinColumns = { @JoinColumn(name = "turista_user_id") },
            inverseJoinColumns = { @JoinColumn(name = "organizzatore_user_id") }
    )
    private List<Organizzatore> seguiti;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "iscrizione",
            joinColumns = { @JoinColumn(name = "turista_user_id") },
            inverseJoinColumns = { @JoinColumn(name = "evento_id") }
    )
    private List<Evento> iscrizioni;

    public Turista(String email, String password, String username, String nome, String cognome) {
        super(email, password, username, Ruolo.TURISTA, nome, cognome);
        this.iscrittoNewsletter = false;
    }
}
