package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.dto.response.DeleteUserResponse;
import it.polimi.iswpf.dto.response.UserResponse;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.service._interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * @return DTO con tutti i dati dell'utente richiesto -> {@link UserResponse}.
     */
    @GetMapping("/getUserData/{userId}")
    public ResponseEntity<UserResponse> getUserData(@PathVariable String userId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserData(Long.parseLong(userId)));
    }

    /**
     * Metodo che modifica i dati di uno specifico utente.
     * @param request DTO con tutti i nuovi dati da sovrascrivere -> {@link UpdateUserDataRequest}.
     * @param userId Id dell'utente, passato in modo dinamico con l'endpoint.
     * @return DTO con i dati dell'utente modificati -> {@link UserResponse}.
     */
    @PutMapping("/updateUserData/{userId}")
    public ResponseEntity<UserResponse> updateUserData(
            @RequestBody UpdateUserDataRequest request,
            @PathVariable String userId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUserData(Long.parseLong(userId), request));
    }

    /**
     * Metodo che permette all'admin di modificare i dati di un utente.
     * @param request DTO con tutti i nuovi dati da sovrascrivere -> {@link UpdateUserDataRequest}.
     * @param username Username dell'utente da modificare, passato in modo dinamico con l'endpoint.
     * @return DTO con i dati dell'utente modificati -> {@link UserResponse}.
     */
    @PutMapping("/adminUpdateUserData/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> adminUpdateUserData(
            @RequestBody UpdateUserDataRequest request,
            @PathVariable String username) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.adminUpdateUserData(username, request));
    }

    /**
     * Metodo che ritorna tutti gli utenti di uno specifico ruolo. Solo l'admin può accederci.
     * @param ruolo Ruolo passato in modo dinamico con l'endpoint.
     * @return Lista di DTO con i dati degli utenti con quel ruolo -> {@link UserResponse}.
     */
    @GetMapping("/getAll/{ruolo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll(@PathVariable String ruolo) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAll(ruolo));
    }

    /**
     * Metodo per eliminare un utente dal database.
     * @param userId Id dell'utente, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di risposta al client.
     */
    @DeleteMapping("delete/{userId}")
    public ResponseEntity<DeleteUserResponse> deleteAccount(@PathVariable String userId) {

        userService.deleteAccount(Long.parseLong(userId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DeleteUserResponse("Account eliminato con successo"));
    }

    /**
     * Dato un username in modo dinamico tramite l'endpoint, viene restituito al client l'utente con quell'username.
     * @param username Username dell'utente di cui si vogliono i dati.
     * @return DTO con i dati dell'utente richiesto -> {@link UserResponse}.
     */
    @GetMapping("getAdminUserData/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getAdminUserData(@PathVariable String username) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAdminUserData(username));
    }

    /**
     * Dato un username dinamico con l'endpoint, l'admin può eliminare altri account.
     * @param username Username dell'account da eliminare.
     * @return Messaggio di risposta al client.
     */
    @DeleteMapping("adminDelete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeleteUserResponse> adminDeleteAccount(@PathVariable String username) {

        userService.adminDeleteAccount(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DeleteUserResponse("Account eliminato con successo"));
    }
}
