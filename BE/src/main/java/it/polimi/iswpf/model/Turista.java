package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.TuristaBuilder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@DiscriminatorValue(value = "turista")
@Entity(name = "turista")
@Table(name = "turista")
public class Turista extends NormalUser {

    @Column(name = "iscritto_newsletter", nullable = false, columnDefinition = "boolean")
    private boolean iscrittoNewsletter;

    @OneToMany(mappedBy = "turista", fetch = FetchType.LAZY)
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
