package it.polimi.iswpf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "_admin")
@Table(name = "_admin")
public class Admin extends User {
}
