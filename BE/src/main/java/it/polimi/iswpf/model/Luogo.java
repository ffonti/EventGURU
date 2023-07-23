package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "luogo")
@Table(name = "luogo")
public class Luogo {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga) */
    @Id
    @SequenceGenerator(
            name = "newsletter_sequence",
            sequenceName = "newsletter_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "newsletter_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "luogo_id", updatable = false, nullable = false)
    private Long luogoId;

    @Column(name = "nome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Column(name = "lat", columnDefinition = "VARCHAR(20)", updatable = false)
    private String lat;

    @Column(name = "lng", columnDefinition = "VARCHAR(20)", updatable = false)
    private String lng;

    @OneToMany(mappedBy = "luogo", fetch = FetchType.LAZY)
    private List<Evento> eventi;
}
