package it.polimi.iswpf.exception;

public class CampoVuotoException extends RuntimeException {

    public CampoVuotoException() {
        super("I campi non devono essere vuoti");
    }
}
