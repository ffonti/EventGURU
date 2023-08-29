package it.polimi.iswpf.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Model delle exceptions customizzate, con solamente messaggio e status.
 */
@Getter
@AllArgsConstructor
public class Exception {

    private String message;

    private HttpStatus status;
}
