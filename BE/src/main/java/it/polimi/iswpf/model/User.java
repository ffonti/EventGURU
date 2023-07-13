package it.polimi.iswpf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @- Model che rappresenta l'utente sul db.
 * @- Implementa UserDetails, un'interfaccia di Spring Security che espone una serie di metodi utili
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "_user",
        uniqueConstraints = @UniqueConstraint(
                name = "username_unique",
                columnNames = "username"
        )
)
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "user_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    private String nome;

    private String cognome;

    private String email;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    private boolean iscritto;

    /**
     * In base al ruolo, viene generata una lista delle funzionalità concesse.
     * @return le autorità garantite all'utente.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruolo.name()));
    }

    /**
     * @return la password dell'utente.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return l'username univoco dell'utente.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indica se l'account dell'utente è scaduto. Un account scaduto non può essere autenticato.
     * @return "true" se l'account è valido (non scaduto), "false" se non è valido (scaduto).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; //Non è stata gestita la scadenza del token, quindi ritorna "true" a prescindere
    }

    /**
     * Indica se l'account dell'utente è bloccato o meno. Un account bloccato non può essere autenticato.
     * @return "true" se l'account non è bloccato, "false" se l'account è bloccato.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se la password dell'utente è scaduta. La password scaduta non permette l'autenticazione.
     * @return "true" se la password è valida (non scaduta), "false" se non è più valida (scaduta).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se l'utente è abilitato o disabilitato. Un utente disabilitato non può essere autenticato.
     * @return "true" se l'utente è abilitato, "false" se l'utente non è abilitato.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
