package it.polimi.iswpf.controller;

import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.model.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller per la gestione delle eccezioni customizzate.
 */
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(InternalServerErrorException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Eccezione chiamata quando la registrazione non è andata a buon fine.
     * @param e Messaggio dell'eccezione.
     * @return Risposta al client con messaggio e status.
     */
//    @ExceptionHandler(value = RegistrazioneNonRiuscitaException.class)
//    public ResponseEntity<Object> handleRegistrazioneNonRiuscitaException(RegistrazioneNonRiuscitaException e) {
//
//        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//
//        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
