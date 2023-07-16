package it.polimi.iswpf.exception;

/**
 * Classe da chiamare ogni tal volta bisogna mandare un'eccezione.
 * Nel costruttore viene specificato il messaggio dell'eccezione stessa.
 */
public class ApiRequestException extends RuntimeException {
    /**
     * Costruttore per lanciare l'eccezione.
     * @param message Messaggio dell'eccezione stessa.
     */
    public ApiRequestException(String message) {
        super(message);
    }
}
