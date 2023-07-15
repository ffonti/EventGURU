package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.service.implementation.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
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

    private final AuthenticationServiceImpl authenticationService;

    /**
     * Metodo per la registrazione. Chiama il service che chiamerà la repository.
     * @param request DTO con i dati per la registrazione -> {@link RegisterRequest }.
     * @return Risposta al client.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws Exception {
        authenticationService.register(request);
        return ResponseEntity.ok("Registrazione completata");
    }

    /**
     * Metodo per il login. Chiama il service che chiamerà la repository.
     * @param request DTO con i dati per il login -> {@link LoginRequest }.
     * @return DTO con il jwt -> {@link LoginResponse }.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
