package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.Evento;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model Evento e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class EventoBuilder {

    private Long eventoId;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataCreazione;
    private String lat;
    private String lng;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param id Valore dell'id univoco.
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
     * @param dataCreazione Stringa con la data di creazione dell'oggetto settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder dataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param lat Stringa con la latitudine del marker settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder lat(String lat) {
        this.lat = lat;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param lng Stringa con la longitudine del marker settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder lng(String lng) {
        this.lng = lng;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataInizio Stringa con la data d'inizio dell'evento settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder dataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param dataFine Stringa con la data di fine dell'evento settata.
     * @return Un'istanza della classe stessa.
     */
    public EventoBuilder dataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
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
