package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.Turista;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model Turista e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class TuristaBuilder {

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
    public TuristaBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param nome Stringa con il nome settato.
     * @return Un'istanza della classe stessa.
     */
    public TuristaBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param cognome Stringa con il cognome settato.
     * @return Un'istanza della classe stessa.
     */
    public TuristaBuilder cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param email Stringa con l'email settata.
     * @return Un'istanza della classe stessa.
     */
    public TuristaBuilder email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param username Stringa con l'username settato.
     * @return Un'istanza della classe stessa.
     */
    public TuristaBuilder username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param password Stringa con la password settata.
     * @return Un'istanza della classe stessa.
     */
    public TuristaBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param ruolo Enum con il ruolo settato.
     * @return Un'istanza della classe stessa.
     */
    public TuristaBuilder ruolo() {
        this.ruolo = Ruolo.TURISTA;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param iscrittoNewsletter Booleano che indica se è iscritto o meno alla newsletter.
     * @return Un'istanza della classe stessa.
     */
    public TuristaBuilder iscrittoNewsletter(boolean iscrittoNewsletter) {
        this.iscrittoNewsletter = iscrittoNewsletter;
        return this;
    }

    /**
     * Metodo che costruisce l'oggetto {@link Turista} con tutti i dati settati in precedenza.
     * @return Un'istanza di Turista a cui viene passato, tramite il costruttore, l'istanza di questa classe.
     */
    public Turista build() {
//        return new Turista(this);
        return null;
    }
}
