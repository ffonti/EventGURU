package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @- Model che rappresenta l'associazione N:M tra user e user.
 * @- Gli utenti possono seguirsi tra loro, quindi come da prassi è stata
 * creata una seconda tabella con cui collegare 1:N e M:1 gli utenti.
 */
@Data
@NoArgsConstructor
@Entity(name = "Segue")
@Table(name = "segue")
public class Segue {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga) */
    @Id
    @SequenceGenerator(
            name = "segue_sequence",
            sequenceName = "segue_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "segue_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_seguace")
    private User seguace;

    @ManyToOne
    @JoinColumn(name = "id_seguito")
    private User seguito;
}
