package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.LuogoBuilder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * Model che rappresenta il luogo sul db.
 */
@Data
@NoArgsConstructor
@Entity(name = "Luogo")
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

    @Column(name = "lat", columnDefinition = "VARCHAR(20)", updatable = false, nullable = false)
    private String lat;

    @Column(name = "lng", columnDefinition = "VARCHAR(20)", updatable = false, nullable = false)
    private String lng;

    @OneToMany(mappedBy = "luogo", fetch = FetchType.LAZY)
    private List<Evento> eventi; //Eventi di un dato luogo.

    /**
     * Design pattern builder. Costruttore dove assegno agli attributi del model i valori
     * settati con il builder (viene eseguito alla chiamata del metodo build() di {@link LuogoBuilder}).
     * @param builder Dati appena settati tramite il pattern.
     */
    public Luogo(@NonNull LuogoBuilder builder) {
        this.luogoId = builder.getLuogoId();
        this.nome = builder.getNome();
        this.lat = builder.getLat();
        this.lng = builder.getLng();
        this.eventi = builder.getEventi();
    }
}
