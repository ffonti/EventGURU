package it.polimi.iswpf.controller;

import it.polimi.iswpf.exception.RegistrazioneNonRiuscitaException;
import it.polimi.iswpf.exception.RuoloInesistenteException;
import it.polimi.iswpf.exception.UsernameRegistratoException;
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

    @ExceptionHandler(value = UsernameRegistratoException.class)
    public ResponseEntity<Object> handleUsernameRegistratoException(UsernameRegistratoException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = RuoloInesistenteException.class)
    public ResponseEntity<Object> handleRuoloInesistenteException(RuoloInesistenteException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = RegistrazioneNonRiuscitaException.class)
    public ResponseEntity<Object> handleRegistrazioneNonRiuscitaException(RegistrazioneNonRiuscitaException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
