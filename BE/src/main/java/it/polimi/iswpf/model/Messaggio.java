package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.MessaggioBuilder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * Model che rappresenta l'associazione N:M tra user ed evento. Gli utenti possono
 * recensire a più eventi e un evento può recensito da più utenti, quindi come da prassi
 * è stata creata una terza tabella con cui collegare 1:N e M:1 gli utenti e gli eventi.
 */
@Data
@NoArgsConstructor
@Entity(name = "Messaggio")
@Table(name = "messaggio")
public class Messaggio {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga) */
    @Id
    @SequenceGenerator(
            name = "messaggio_sequence",
            sequenceName = "messaggio_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "messaggio_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "messaggio_id", updatable = false, nullable = false)
    private Long messaggioId;

    @Column(name = "testo", columnDefinition = "VARCHAR(1000)", nullable = false)
    private String testo;

    @Column(name = "data_invio", nullable = false, updatable = false)
    private LocalDateTime dataInvio;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //Utente che scrive il messaggio.

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento; //Evento a cui viene scritto il messaggio.

    /**
     * Design pattern builder. Costruttore dove assegno agli attributi del model i valori
     * settati con il builder (viene eseguito alla chiamata del metodo build() di {@link MessaggioBuilder}).
     * @param builder dati appena settati tramite il pattern.
     */
    public Messaggio(@NonNull MessaggioBuilder builder) {
        this.messaggioId = builder.getMessaggioId();
        this.testo = builder.getTesto();
        this.dataInvio = builder.getDataInvio();
        this.user = builder.getUser();
        this.evento = builder.getEvento();
    }
}