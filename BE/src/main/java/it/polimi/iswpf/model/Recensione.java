package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.RecensioneBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @- Model che rappresenta l'associazione N:M tra user ed evento. Gli utenti possono
 * recensire a più eventi e un evento può recensito da più utenti, quindi come da prassi
 * è stata creata una terza tabella con cui collegare 1:N e M:1 gli utenti e gli eventi.
 */
@Data
@NoArgsConstructor
@Entity(name = "Recensione")
@Table(name = "recensione")
public class Recensione {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga) */
    @Id
    @SequenceGenerator(
            name = "recensione_sequence",
            sequenceName = "recensione_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "recensione_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "testo", columnDefinition = "VARCHAR(1000)")
    private String testo;

    @Column(name = "voto", nullable = false)
    @Min(1)
    @Max(5)
    private Integer voto;

//    @ManyToOne
//    @JoinColumn(name = "id_user")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "id_evento")
//    private Evento evento;

    /**
     * Design pattern builder. Costruttore dove assegno agli attributi del model i valori
     * settati con il builder (viene eseguito alla chiamata del metodo build() di {@link RecensioneBuilder}).
     * @param builder dati appena settati tramite il pattern.
     */
    public Recensione(@NonNull RecensioneBuilder builder) {
        this.id = builder.getId();
        this.testo = builder.getTesto();
        this.voto = builder.getVoto();
    }
}