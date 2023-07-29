package it.polimi.iswpf.exception;

public class PasswordUgualiException extends RuntimeException {

    public PasswordUgualiException() {
        super("Le due password sono uguali");
    }
}
