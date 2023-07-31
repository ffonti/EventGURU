package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.dto.response.RegisterResponse;
import it.polimi.iswpf.service._interface.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @return Messaggio di avvenuta registrazione.
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {

        authenticationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse("Registrazione completata!"));
    }

    /**
     * Metodo per il login. Chiama il service che chiamerà la repository col fine di autenticare l'utente.
     * @param request DTO con i dati per il login -> {@link LoginRequest}.
     * @return {@link LoginResponse} DTO con l'utente autenticato e un messaggio di avvenuta registrazione.
     * Nell'header la stringa jwt.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {

        final LoginResponse response = authenticationService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                //Come da prassi, la stringa jwt va inserita nell'header della chiamata.
                .headers(authenticationService.putJwtInHttpHeaders(response.getJwt()))
                .body(response);
    }
}
