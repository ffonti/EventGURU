package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@DiscriminatorValue(value = "organizzatore")
@Entity(name = "organizzatore")
@Table(name = "organizzatore")
public class Organizzatore extends NormalUser {

    @Column(name = "nome_compagnia", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nomeCompagnia;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "segue",
            joinColumns = { @JoinColumn(name = "organizzatore_user_id") },
            inverseJoinColumns = { @JoinColumn(name = "turista_user_id") }
    )
    private List<Turista> seguaci;

    @OneToMany(mappedBy = "organizzatore", fetch = FetchType.LAZY)
    private List<Evento> eventi;
}
