package it.polimi.iswpf.exception;

public class PasswordErrataException extends RuntimeException {

    public PasswordErrataException() {
        super("Password errata");
    }
}
