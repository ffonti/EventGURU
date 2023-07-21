package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.EventoBuilder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @- Model che rappresenta l'evento sul db.
 */
@Data
@NoArgsConstructor
@Entity(name = "Evento")
@Table(
        name = "evento",
        uniqueConstraints = @UniqueConstraint(
                name = "titolo_unique",
                columnNames = "titolo"
        )
)
public class Evento {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga) */
    @Id
    @SequenceGenerator(
            name = "evento_sequence",
            sequenceName = "evento_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "evento_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "evento_id", updatable = false, nullable = false)
    private Long eventoId;

    @Column(name = "titolo", nullable = false, columnDefinition = "VARCHAR(100)", unique = true)
    private String titolo;

    @Column(name = "descrizione", nullable = false, columnDefinition = "VARCHAR(1000)")
    private String descrizione;

    @Column(name = "data_creazione", nullable = false, updatable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "lat", columnDefinition = "VARCHAR(20)", updatable = false)
    private String lat;

    @Column(name = "lng", columnDefinition = "VARCHAR(20)", updatable = false)
    private String lng;

    @Column(name = "data_inizio", nullable = false, updatable = false)
    private LocalDateTime dataInizio;

    @Column(name = "data_fine", nullable = false, updatable = false)
    private LocalDateTime dataFine;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "iscrizione",
            joinColumns = { @JoinColumn(name = "evento_id") },
            inverseJoinColumns = { @JoinColumn(name = "turista_user_id") }
    )
    private List<Turista> iscritti;

    @OneToMany(mappedBy = "evento")
    private List<Recensione> recensioni;

    @ManyToOne
    @JoinColumn(name = "organizzatore_user_id")
    private Organizzatore organizzatore;

    /**
     * Design pattern builder. Costruttore dove assegno agli attributi del model i valori
     * settati con il builder (viene eseguito alla chiamata del metodo build() di {@link EventoBuilder}).
     * @param builder dati appena settati tramite il pattern.
     */
    public Evento(@NonNull EventoBuilder builder) {
        this.eventoId = builder.getEventoId();
        this.titolo = builder.getTitolo();
        this.descrizione = builder.getDescrizione();
        this.dataCreazione = builder.getDataCreazione();
        this.lat = builder.getLat();
        this.lng = builder.getLng();
        this.dataInizio = builder.getDataInizio();
        this.dataFine = builder.getDataFine();
    }
}
