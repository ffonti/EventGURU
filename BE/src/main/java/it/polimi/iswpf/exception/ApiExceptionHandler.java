package it.polimi.iswpf.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Handler per le eccezioni.
 */
@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * Metodo che gestisce le eccezioni. Assegna i dati a un'istanza e la ritorna.
     * @param e Messaggio dell'eccezione.
     * @return Un'istanza dell'eccezione con messaggio, status code e timestamp dell'errore.
     */
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(@NonNull ApiRequestException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
