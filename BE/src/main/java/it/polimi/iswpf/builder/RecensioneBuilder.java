package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.entity.Evento;
import it.polimi.iswpf.model.entity.Recensione;
import it.polimi.iswpf.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model Recensione e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class RecensioneBuilder {

    //Attributi del model Recensione
    private Long recensioneId;
    private String testo;
    private Integer voto;
    private LocalDateTime dataCreazione;
    private User user;
    private Evento evento;

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param recensioneId Valore dell'id univoco.
     * @return Un'istanza della classe stessa.
     */
    public RecensioneBuilder recensioneId(Long recensioneId) {
        this.recensioneId = recensioneId;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param testo Stringa con il testo settato.
     * @return Un'istanza della classe stessa.
     */
    public RecensioneBuilder testo(String testo) {
        this.testo = testo;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param voto Valore del voto settato.
     * @return Un'istanza della classe stessa.
     */
    public RecensioneBuilder voto(Integer voto) {
        this.voto = voto;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataCreazione Data di creazione della recensione.
     * @return Un'istanza della classe stessa.
     */
    public RecensioneBuilder dataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param user Turista che recensisce.
     * @return Un'istanza della classe stessa.
     */
    public RecensioneBuilder user(User user) {
        this.user = user;
        return this;
    }

    /**
     * Chiamato prima del meotodo build(), per settare l'attributo.
     * @param evento Evento recensito.
     * @return Un'istanza della classe stessa.
     */
    public RecensioneBuilder evento(Evento evento) {
        this.evento = evento;
        return this;
    }

    /**
     * Metodo che costruisce l'oggetto {@link Recensione} con tutti i dati settati in precedenza.
     * @return Un'istanza di Recensione a cui viene passato, tramite il costruttore, l'istanza di questa classe.
     */
    public Recensione build() {
        return new Recensione(this);
    }
}
