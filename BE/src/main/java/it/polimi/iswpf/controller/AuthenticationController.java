package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.exception.RuoloNonValidoException;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.service._interface.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller per l'autenticazione.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Metodo per la registrazione. Chiama il service che chiamerà
     * la repository col fine di registrare l'utente sul database.
     * @param request DTO con i dati per la registrazione -> {@link RegisterRequest}.
     * @return Risposta al client.
     * @throws RuntimeException insieme di eccezioni customizzate che estendono RuntimeException
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws RuntimeException {

        authenticationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registrazione completata!");
    }

    /**
     * Metodo per il login. Chiama il service che chiamerà la repository col fine di autenticare l'utente.
     * @param request DTO con i dati per il login -> {@link LoginRequest}.
     * @return Risposta al client
     */
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest request) {
        final String jwt = authenticationService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(authenticationService.putJwtInHttpHeaders(jwt))
                .body("Accesso eseguito!");
    }
}
