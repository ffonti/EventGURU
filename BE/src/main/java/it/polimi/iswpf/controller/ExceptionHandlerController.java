package it.polimi.iswpf.controller;

import it.polimi.iswpf.model.Exception;
import it.polimi.iswpf.exception.RuoloNonValidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = RuoloNonValidoException.class)
    public ResponseEntity<Object> handleRuoloNonValidoException(RuoloNonValidoException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }
}
