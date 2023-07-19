package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @- Model che rappresenta l'associazione N:M tra user ed evento. Gli utenti possono
 * iscriversi a più eventi e un evento può ricevere più iscrizioni, quindi come da prassi
 * è stata creata una terza tabella con cui collegare 1:N e M:1 gli utenti e gli eventi.
 */
@Data
@NoArgsConstructor
@Entity(name = "Iscrizione")
@Table(name = "iscrizione")
public class Iscrizione {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga) */
    @Id
    @SequenceGenerator(
            name = "iscrizione_sequence",
            sequenceName = "iscrizione_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "iscrizione_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento evento;
}
