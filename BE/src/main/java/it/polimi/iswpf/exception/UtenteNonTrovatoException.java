package it.polimi.iswpf.exception;

public class UtenteNonTrovatoException extends RuntimeException {
    public UtenteNonTrovatoException() {
        super("Utente non trovato");
    }
}
