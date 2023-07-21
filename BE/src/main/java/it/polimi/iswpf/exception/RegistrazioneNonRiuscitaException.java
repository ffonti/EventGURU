package it.polimi.iswpf.exception;

public class RegistrazioneNonRiuscitaException extends RuntimeException {

    public RegistrazioneNonRiuscitaException() {
        super("Errore nella registrazione");
    }
}
