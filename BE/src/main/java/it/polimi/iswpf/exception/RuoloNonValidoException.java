package it.polimi.iswpf.exception;

public class RuoloNonValidoException extends RuntimeException {
    public RuoloNonValidoException() {
        super("Il ruolo scelto per la registrazione non è valido");
    }
}
