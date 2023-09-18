package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.RecensioneBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * Model che rappresenta l'associazione N:M tra user ed evento. Gli utenti possono
 * recensire a più eventi e un evento può recensito da più utenti, quindi come da prassi
 * è stata creata una terza tabella con cui collegare 1:N e M:1 gli utenti e gli eventi
 * in modo da poter aggiungere altri attributi utili.
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
    @Column(name = "recensione_id", nullable = false, updatable = false)
    private Long recensioneId;

    @Column(name = "testo", updatable = false, columnDefinition = "VARCHAR(1000)")
    private String testo;

    @Column(name = "voto", nullable = false, updatable = false)
    @Min(1)
    @Max(5)
    private Integer voto;

    @Column(name = "data_creazione", updatable = false)
    private LocalDateTime dataCreazione;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //Utente che recensisce.

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento; //Evento recensito.

    /**
     * Design pattern builder. Costruttore dove assegno agli attributi del model i valori
     * settati con il builder (viene eseguito alla chiamata del metodo build() di {@link RecensioneBuilder}).
     * @param builder Dati appena settati tramite il pattern.
     */
    public Recensione(@NonNull RecensioneBuilder builder) {
        this.recensioneId = builder.getRecensioneId();
        this.testo = builder.getTesto();
        this.voto = builder.getVoto();
        this.dataCreazione = builder.getDataCreazione();
        this.user = builder.getUser();
        this.evento = builder.getEvento();
    }
}