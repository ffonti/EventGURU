package it.polimi.iswpf.exception;

/**
 * Classe per la gestione delle eccezioni con status code 403.
 * Estende {@link RuntimeException}, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della JVM.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
