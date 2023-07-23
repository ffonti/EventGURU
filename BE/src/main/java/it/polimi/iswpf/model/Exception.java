package it.polimi.iswpf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Exception {

    private String message;
    private HttpStatus status;
}
