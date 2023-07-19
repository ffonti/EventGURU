package it.polimi.iswpf.model;

import it.polimi.iswpf.builder.UserBuilder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @- Model che rappresenta l'utente sul db.
 * @- Implementa UserDetails, un'interfaccia di Spring Security che espone una serie di metodi utili per la sicurezza.
 */
@Data
@NoArgsConstructor
@Entity(name = "User")
@Table(
        name = "_user",
        uniqueConstraints = @UniqueConstraint(
                name = "username_unique",
                columnNames = "username"
        )
)
public class User implements UserDetails {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga) */
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
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Column(name = "cognome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String cognome;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "username", nullable = false, columnDefinition = "VARCHAR(50)", unique = true)
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(name = "ruolo", nullable = false, updatable = false, columnDefinition = "VARCHAR(5)")
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @Column(name = "iscritto_newsletter", nullable = false)
    private boolean iscrittoNewsletter;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recensione> recensioni;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Iscrizione> iscrizioni;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> seguaci;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> seguiti;

    /**
     * Design pattern builder. Costruttore dove assegno agli attributi del model i valori
     * settati con il builder (viene eseguito alla chiamata del metodo build() di {@link UserBuilder}).
     * @param builder dati appena settati tramite il pattern.
     */
    public User(@NonNull UserBuilder builder) {
        this.id = builder.getId();
        this.nome = builder.getNome();
        this.cognome = builder.getCognome();
        this.email = builder.getEmail();
        this.username = builder.getUsername();
        this.password = builder.getPassword();
        this.ruolo = builder.getRuolo();
        this.iscrittoNewsletter = builder.isIscrittoNewsletter();
    }

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
        return true; //Non è stata gestita la scadenza del token, quindi ritorna "true" a prescindere.
    }

    /**
     * Indica se l'account dell'utente è bloccato o meno. Un account bloccato non può essere autenticato.
     * @return "true" se l'account non è bloccato, "false" se l'account è bloccato.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; //Non è stato gestito il blocco, quindi ritorna "true" a prescindere.
    }

    /**
     * Indica se la password dell'utente è scaduta. La password scaduta non permette l'autenticazione.
     * @return "true" se la password è valida (non scaduta), "false" se non è più valida (scaduta).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //Non è stata gestita la scadenza delle credenziali, quindi ritorna "true" a prescindere.
    }

    /**
     * Indica se l'utente è abilitato o disabilitato. Un utente disabilitato non può essere autenticato.
     * @return "true" se l'utente è abilitato, "false" se l'utente non è abilitato.
     */
    @Override
    public boolean isEnabled() {
        return true; //Non è stata gestita l'abilitazione dell'utente, quindi ritorna "true" a prescindere.
    }
}