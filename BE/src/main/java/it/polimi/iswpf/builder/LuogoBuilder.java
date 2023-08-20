package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Luogo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model Recensione e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class LuogoBuilder {

    //Attributi del model Luogo
    private Long luogoId;
    private String nome;
    private String lat;
    private String lng;
    private List<Evento> eventi;

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param luogoId Valore dell'id univoco.
     * @return Un'istanza della classe stessa.
     */
    public LuogoBuilder luogoId(Long luogoId) {
        this.luogoId = luogoId;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param nome Stringa con il nome settato.
     * @return Un'istanza della classe stessa.
     */
    public LuogoBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param lat Valore della latitudine settato.
     * @return Un'istanza della classe stessa.
     */
    public LuogoBuilder lat(String lat) {
        this.lat = lat;
        return this;
    }

    /**
     * Chiamato prima del metodo build(), per settare l'attributo.
     * @param lng Valore della longitudine settato.
     * @return Un'istanza della classe stessa.
     */
    public LuogoBuilder lng(String lng) {
        this.lng = lng;
        return this;
    }

    /**
     * Chiamato prima del meotodo build(), per settare l'attributo.
     * @param eventi Lista di eventi svolti in quel luogo.
     * @return Un'istanza della classe stessa.
     */
    public LuogoBuilder eventi(List<Evento> eventi) {
        this.eventi = eventi;
        return this;
    }

    /**
     * Metodo che costruisce l'oggetto {@link Luogo} con tutti i dati settati in precedenza.
     * @return Un'istanza di Recensione a cui viene passato, tramite il costruttore, l'istanza di questa classe.
     */
    public Luogo build() {
        return new Luogo(this);
    }
}
