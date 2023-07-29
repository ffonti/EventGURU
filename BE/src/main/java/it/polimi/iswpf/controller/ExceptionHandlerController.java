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
     * Eccezione chiamata quando il ruolo richiesto per la registrazione non è valido (ADMIN).
     * @param e Messaggio dell'eccezione.
     * @return Risposta al client con messaggio e status.
     */
    @ExceptionHandler(value = RuoloNonValidoException.class)
    public ResponseEntity<Object> handleRuoloNonValidoException(RuoloNonValidoException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    /**
     * Eccezione chiamata quando si prova a registrare un username già presente sul database.
     * @param e Messaggio dell'eccezione.
     * @return Risposta al client con messaggio e status.
     */
    @ExceptionHandler(value = UsernameRegistratoException.class)
    public ResponseEntity<Object> handleUsernameRegistratoException(UsernameRegistratoException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
    }

    /**
     * Eccezione chiamata quando il ruolo richiesto per la registrazione non esiste.
     * @param e Messaggio dell'eccezione.
     * @return Risposta al client con messaggio e status.
     */
    @ExceptionHandler(value = RuoloInesistenteException.class)
    public ResponseEntity<Object> handleRuoloInesistenteException(RuoloInesistenteException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    /**
     * Eccezione chiamata quando la registrazione non è andata a buon fine.
     * @param e Messaggio dell'eccezione.
     * @return Risposta al client con messaggio e status.
     */
    @ExceptionHandler(value = RegistrazioneNonRiuscitaException.class)
    public ResponseEntity<Object> handleRegistrazioneNonRiuscitaException(RegistrazioneNonRiuscitaException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Eccezione chiamata quando sono presenti dei campi vuoti tra i dati.
     * @param e Messaggio dell'eccezione.
     * @return Risposta al client con messaggio e status.
     */
    @ExceptionHandler(value = CampoVuotoException.class)
    public ResponseEntity<Object> handleCampoVuotoException(CampoVuotoException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UtenteNonTrovatoException.class)
    public ResponseEntity<Object> handleUtenteNonTrovatoException(UtenteNonTrovatoException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IdNonValidoException.class)
    public ResponseEntity<Object> handleIdNonValidoException(IdNonValidoException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PasswordUgualiException.class)
    public ResponseEntity<Object> handlePasswordUgualiException(PasswordUgualiException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(customException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = PasswordErrataException.class)
    public ResponseEntity<Object> handlePasswordErrataException(PasswordErrataException e) {

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }
}
