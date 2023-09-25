package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model User e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class UserBuilder {

    //Attributi del model User
    private Long userId;
    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String password;
    private Ruolo ruolo;
    private boolean iscrittoNewsletter;
    private LocalDateTime dataCreazione;
    private List<Evento> eventi;
    private List<Recensione> recensioni;
    private List<User> seguiti;
    private List<Evento> iscrizioni;

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param userId Valore dell'id univoco.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param nome Stringa con il nome settato.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param cognome Stringa con il cognome settato.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param email Stringa con l'email settata.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param username Stringa con l'username settato.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param password Stringa con la password settata.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param ruolo Enum con il ruolo settato.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder ruolo(Ruolo ruolo) {
        this.ruolo = ruolo;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param iscrittoNewsletter Booleano che indica se l'utente è iscritto o meno alla newsletter.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder iscrittoNewsletter(boolean iscrittoNewsletter) {
        this.iscrittoNewsletter = iscrittoNewsletter;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataCreazione Data di registrazione dell'utente.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder dataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param eventi Lista di eventi gestiti dall'organizzatore.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder eventi(List<Evento> eventi) {
        this.eventi = eventi;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param recensioni Lista di recensioni lasciate dal turista.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder recensioni(List<Recensione> recensioni) {
        this.recensioni = recensioni;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param seguiti Lista di organizzatori seguiti dal turista.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder seguiti(List<User> seguiti) {
        this.seguiti = seguiti;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param iscrizioni Eventi a cui il turista è iscritto.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder iscrizioni(List<Evento> iscrizioni) {
        this.iscrizioni = iscrizioni;
        return this;
    }

    /**
     * Metodo che costruisce l'oggetto {@link User} con tutti i dati settati in precedenza.
     * @return Un'istanza di User a cui viene passato, tramite il costruttore, l'istanza di questa classe.
     */
    public User build() {
        return new User(this);
    }
}
