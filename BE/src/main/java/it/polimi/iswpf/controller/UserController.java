package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.service._interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per l'user.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    /**
     * Metodo che ritorna i dati di uno specifico utente.
     * @param userId Id dell'utente, passato in modo dinamico con l'endpoint.
     * @return Model {@link User} con tutti i dati.
     */
    @GetMapping("/getUserData/{userId}")
    public ResponseEntity<User> getUserData(@PathVariable String userId) {

        final User response = userService.getUserData(Long.parseLong(userId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo che modifica i dati di uno specifico utente.
     * @param request DTO con tutti i nuovi dati da sovrascrivere -> {@link UpdateUserDataRequest}.
     * @param userId Id dell'utente, passato in modo dinamico con l'endpoint.
     * @return Model {@link User} con i dati modificati.
     */
    @PutMapping("/updateUserData/{userId}")
    public ResponseEntity<User> updateUserData(
            @RequestBody UpdateUserDataRequest request,
            @PathVariable String userId) {

        final User response = userService.updateUserData(Long.parseLong(userId), request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo che ritorna tutti gli utenti di uno specifico ruolo.
     * @param ruolo Ruolo passato in modo dinamico con l'endpoint.
     * @return Lista di utenti con quel ruolo.
     */
    @GetMapping("/getAll/{ruolo}")
    public ResponseEntity<List<User>> getAll(@PathVariable String ruolo) {

        final List<User> response = userService.getAll(ruolo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
