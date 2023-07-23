package it.polimi.iswpf.builder;

import it.polimi.iswpf.model.Recensione;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Design pattern builder. Classe Builder dove vengono riportati esattamente tutti
 * gli attributi del model Recensione e un rispettivo metodo (setter) per ognuno di loro.
 */
@Getter
@NoArgsConstructor
public class RecensioneBuilder {

    private Long recensioneId;
    private String testo;
    private Integer voto;

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
     * Metodo che costruisce l'oggetto {@link Recensione} con tutti i dati settati in precedenza.
     * @return Un'istanza di Recensione a cui viene passato, tramite il costruttore, l'istanza di questa classe.
     */
    public Recensione build() {
        return new Recensione(this);
    }
}
