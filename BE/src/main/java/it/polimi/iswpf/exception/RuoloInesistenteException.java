package it.polimi.iswpf.exception;

public class RuoloInesistenteException extends RuntimeException {

    public RuoloInesistenteException() {
        super("Il ruolo non esiste");
    }
}
