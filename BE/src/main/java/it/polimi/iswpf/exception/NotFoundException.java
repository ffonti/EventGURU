package it.polimi.iswpf.exception;

/**
 * Classe per la gestione delle eccezioni con status code 404.
 * Estende {@link RuntimeException}, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della JVM.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {

        super(message);
    }
}
