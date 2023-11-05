package it.polimi.iswpf.model.entity;

import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.model._enum.Ruolo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Model che rappresenta l'utente sul db. Implementa UserDetails, un'interfaccia
 * di Spring Security che espone una serie di metodi utili per la sicurezza.
 */
@Data
@NoArgsConstructor
@Entity(name = "User")
@Table(
        name = "_user", //"user" è una parola riservata in PostgreSQL
        uniqueConstraints = @UniqueConstraint(
                name = "username_unique",
                columnNames = "username"))
public class User implements UserDetails {

    /* @SequenceGenerator e @GeneratedValue servono per
    configurare l'id con l'autoincrement (+1 ogni riga). */
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1)
    @GeneratedValue(
            generator = "user_sequence",
            strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "nome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Column(name = "cognome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String cognome;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(name = "data_creazione", updatable = false)
    private LocalDateTime dataCreazione;

    @Column(name = "ruolo", nullable = false, updatable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @Column(name = "iscritto_newsletter", nullable = false, columnDefinition = "boolean")
    private boolean iscrittoNewsletter;

    @OneToMany(mappedBy = "organizzatore", fetch = FetchType.LAZY)
    private List<Evento> eventi; //Lista di eventi gestiti dall'organizzatore.

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Recensione> recensioni; //Lista di recensioni lasciate dal turista.

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "segue",
            joinColumns = { @JoinColumn(name = "turista_user_id") },
            inverseJoinColumns = { @JoinColumn(name = "organizzatore_user_id") }
    )
    private List<User> seguiti; //Organizzatori seguiti dal turista

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "iscrizione",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "evento_id") }
    )
    private List<Evento> iscrizioni; //Eventi a cui l'utente si è iscritto.

    /**
     * Design pattern builder. Costruttore dove assegno agli attributi del model i valori
     * settati con il builder (viene eseguito alla chiamata del metodo build() di {@link UserBuilder}).
     * @param builder Dati appena settati tramite il pattern.
     */
    public User(@NonNull UserBuilder builder) {
        this.userId = builder.getUserId();
        this.nome = builder.getNome();
        this.cognome = builder.getCognome();
        this.email = builder.getEmail();
        this.username = builder.getUsername();
        this.password = builder.getPassword();
        this.ruolo = builder.getRuolo();
        this.iscrittoNewsletter = builder.isIscrittoNewsletter();
        this.dataCreazione = builder.getDataCreazione();
        this.eventi = builder.getEventi();
        this.recensioni = builder.getRecensioni();
        this.seguiti = builder.getSeguiti();
        this.iscrizioni = builder.getIscrizioni();
    }

    /**
     * Metodo ereditato da {@link UserDetails}. In base al ruolo, viene generata una lista delle funzionalità concesse.
     * @return I permessi dell'utente.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name()));
    }

    /**
     * Metodo ereditato da {@link UserDetails}.
     * @return La password per l'autenticazione.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Metodo ereditato da {@link UserDetails}.
     * @return Il valore univoco per l'autenticazione.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Metodo ereditato da {@link UserDetails}. Indica se l'account dell'utente è scaduto. Un account scaduto non può essere autenticato.
     * @return "true" se l'account è valido (non scaduto), "false" se non è valido (scaduto).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; //Non è stata gestita la scadenza del token, quindi ritorna "true" a prescindere.
    }

    /**
     * Metodo ereditato da {@link UserDetails}. Indica se l'account dell'utente è bloccato o meno. Un account bloccato non può essere autenticato.
     * @return "true" se l'account non è bloccato, "false" se l'account è bloccato.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; //Non è stato gestito il blocco, quindi ritorna "true" a prescindere.
    }

    /**
     * Metodo ereditato da {@link UserDetails}. Indica se la password dell'utente è scaduta. La password scaduta non permette l'autenticazione.
     * @return "true" se la password è valida (non scaduta), "false" se non è più valida (scaduta).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //Non è stata gestita la scadenza delle credenziali, quindi ritorna "true" a prescindere.
    }

    /**
     * Metodo ereditato da {@link UserDetails}. Indica se l'utente è abilitato o disabilitato. Un utente disabilitato non può essere autenticato.
     * @return "true" se l'utente è abilitato, "false" se l'utente non è abilitato.
     */
    @Override
    public boolean isEnabled() {
        return true; //Non è stata gestita l'abilitazione dell'utente, quindi ritorna "true" a prescindere.
    }
}