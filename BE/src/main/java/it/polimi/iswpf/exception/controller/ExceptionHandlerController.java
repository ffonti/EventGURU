package it.polimi.iswpf.exception.controller;

import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.exception.entity.Exception;
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
    public ResponseEntity<Exception> handleBadRequestException(BadRequestException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler delle eccezioni con status 403: il client non ha i permessi adatti.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<Exception> handleForbiddenException(ForbiddenException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(customException, HttpStatus.FORBIDDEN);
    }

    /**
     * Handler delle eccezioni con status 404: il server non è in grado di trovare la risorsa richiesta.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Exception> handleNotFoundException(NotFoundException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler delle eccezioni con status 409: la richiesta è in conflitto con lo stato attuale del server.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Exception> handleConflictException(ConflictException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
    }

    /**
     * Handler delle eccezioni con status 500: il server non è in grado di gestire la richiesta.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al client con messaggio e status.
     */
    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Exception> handleInternalServerErrorException(InternalServerErrorException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
