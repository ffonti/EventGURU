package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

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
}
