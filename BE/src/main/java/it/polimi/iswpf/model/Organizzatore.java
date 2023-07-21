package it.polimi.iswpf.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity(name = "Organizzatore")
@Table(name = "organizzatore")
public class Organizzatore extends NormalUser {

    @Column(name = "nome_compagnia", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nomeCompagnia;
}
