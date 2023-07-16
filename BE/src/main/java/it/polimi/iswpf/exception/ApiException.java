package it.polimi.iswpf.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Model per le exceptions, così da omettere dati superflui in arrivo al client.
 */
@Getter
@AllArgsConstructor
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;
}
