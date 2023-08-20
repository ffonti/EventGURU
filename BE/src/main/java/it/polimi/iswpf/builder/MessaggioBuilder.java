package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Messaggio;
import it.polimi.iswpf.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model Recensione e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class MessaggioBuilder {

    //Attributi del model Messaggio
    private Long messaggioId;
    private String testo;
    private LocalDateTime dataInvio;
    private User user;
    private Evento evento;

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param messaggioId Valore dell'id univoco.
     * @return Un'istanza della classe stessa.
     */
    public MessaggioBuilder messaggioId(Long messaggioId) {
        this.messaggioId = messaggioId;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param testo Stringa con il testo settato.
     * @return Un'istanza della classe stessa.
     */
    public MessaggioBuilder testo(String testo) {
        this.testo = testo;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataInvio Valore della data di invio del messaggio settato.
     * @return Un'istanza della classe stessa.
     */
    public MessaggioBuilder dataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param user Utente che invia il messaggio.
     * @return Un'istanza della classe stessa.
     */
    public MessaggioBuilder user(User user) {
        this.user = user;
        return this;
    }

    /**
     * Chiamato prima del meotodo build(), per settare l'attributo.
     * @param evento Evento a cui si riferisce il messaggio.
     * @return Un'istanza della classe stessa.
     */
    public MessaggioBuilder evento(Evento evento) {
        this.evento = evento;
        return this;
    }

    /**
     * Metodo che costruisce l'oggetto {@link Messaggio} con tutti i dati settati in precedenza.
     * @return Un'istanza di Recensione a cui viene passato, tramite il costruttore, l'istanza di questa classe.
     */
    public Messaggio build() {
        return new Messaggio(this);
    }
}
