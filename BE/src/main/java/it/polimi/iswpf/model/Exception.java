package it.polimi.iswpf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Exception {

    private final String message;
    private final HttpStatus status;
}
