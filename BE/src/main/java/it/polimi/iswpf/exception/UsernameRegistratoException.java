package it.polimi.iswpf.exception;

public class UsernameRegistratoException extends RuntimeException {

    public UsernameRegistratoException() {
        super("Username già registrato");
    }
}
