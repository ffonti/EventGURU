package it.polimi.iswpf.model.entity;

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
@Table(name = "luogo",
        uniqueConstraints = @UniqueConstraint(
        name = "nome_unique",
        columnNames = "nome"))
public class Luogo {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga). */
    @Id
    @SequenceGenerator(
            name = "newsletter_sequence",
            sequenceName = "newsletter_sequence",
            allocationSize = 1)
    @GeneratedValue(
            generator = "newsletter_sequence",
            strategy = GenerationType.SEQUENCE)
    @Column(name = "luogo_id", updatable = false, nullable = false)
    private Long luogoId;

    @Column(name = "nome", nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Column(name = "lat", updatable = false, nullable = false)
    private Float lat;

    @Column(name = "lng", updatable = false, nullable = false)
    private Float lng;

    @OneToMany(mappedBy = "luogo", fetch = FetchType.LAZY)
    private List<Evento> eventi; //Eventi in un dato luogo.

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
