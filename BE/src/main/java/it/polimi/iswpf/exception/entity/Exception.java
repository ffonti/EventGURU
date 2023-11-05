package it.polimi.iswpf.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Model delle custom exception, con messaggio e status. Tramite le apposite
 * annotazioni si hanno i metodi getter e un costruttore con tutti gli attributi.
 */
@Getter
@AllArgsConstructor
public class Exception {

    private String message;

    private HttpStatus status;
}
