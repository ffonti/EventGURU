package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
     * @param iscritto Booleano che indica se è iscritto o meno alla newsletter.
     * @return Un'istanza della classe stessa.
     */
    public UserBuilder iscrittoNewsletter(boolean iscrittoNewsletter) {
        this.iscrittoNewsletter = iscrittoNewsletter;
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
