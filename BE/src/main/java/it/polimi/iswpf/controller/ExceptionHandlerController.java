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

    /**
     * Handler delle eccezioni con status 400: il server non è in grado di processare la richiesta del client.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler delle eccezioni con status 404: il server non è in grado di trovare la risorsa richiesta.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler delle eccezioni con status 409: la richiesta è in conflitto con lo stato attuale del server.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
    }

    /**
     * Handler delle eccezioni con status 500: il server non è in grado di gestire la richiesta.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(InternalServerErrorException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
