package it.polimi.iswpf.exception;

/**
 * Classe per la gestione delle eccezioni con status code 500.
 * Estende {@link RuntimeException}, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della JVM.
 */
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String message) {

        super(message);
    }
}
