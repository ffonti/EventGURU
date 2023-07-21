package it.polimi.iswpf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity(name = "Admin")
@Table(name = "_admin")
public class Admin extends User {
}
