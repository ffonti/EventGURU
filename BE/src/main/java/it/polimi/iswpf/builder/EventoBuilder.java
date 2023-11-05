package it.polimi.iswpf.builder;

import it.polimi.iswpf.model._enum.Stato;
import it.polimi.iswpf.model.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model Evento e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class EventoBuilder {

    //Attributi del model Evento
    private Long eventoId;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataCreazione;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private Stato stato;
    private List<User> iscritti;
    private List<Recensione> recensioni;
    private User organizzatore;
    private Luogo luogo;

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param eventoId Valore dell'id univoco.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder eventoId(Long eventoId) {
        this.eventoId = eventoId;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param titolo Stringa con il titolo settato.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder titolo(String titolo) {
        this.titolo = titolo;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param descrizione Stringa con la descrizione settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder descrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataCreazione Data di creazione dell'evento settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder dataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataInizio Data d'inizio dell'evento settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder dataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataFine Data di fine dell'evento settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder dataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param stato Enum con lo stato dell'evento (passato, presente, futuro).
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder stato(Stato stato) {
        this.stato = stato;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param iscritti Lista di utenti iscritti all'evento.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder iscritti(List<User> iscritti) {
        this.iscritti = iscritti;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param recensioni Lista di recensioni dell'evento.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder recensioni(List<Recensione> recensioni) {
        this.recensioni = recensioni;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param organizzatore Organizzatore dell'evento.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder organizzatore(User organizzatore) {
        this.organizzatore = organizzatore;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param luogo Luogo dell'evento.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder luogo(Luogo luogo) {
        this.luogo = luogo;
        return this;
    }

    /**
     * Metodo che costruisce l'oggetto {@link Evento} con tutti i dati settati in precedenza.
     * @return Un'istanza di Evento a cui viene passato, tramite il costruttore, l'istanza di questa classe.
     */
    public Evento build() {
        return new Evento(this);
    }
}
