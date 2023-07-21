package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity(name = "Turista")
@Table(name = "turista")
public class Turista extends NormalUser {

    @Column(name = "iscritto_newsletter", nullable = false)
    private boolean iscrittoNewsletter;
}
