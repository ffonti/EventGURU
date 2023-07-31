package it.polimi.iswpf.exception;

/**
 * Classe per la gestione delle eccezioni con status code 400.
 * Estende {@link RuntimeException}, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della JVM.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
