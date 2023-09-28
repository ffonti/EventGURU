package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.dto.response.*;
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
     * @return DTO con tutti i dati dell'utente richiesto.
     */
    @GetMapping("/getUserData/{userId}")
    public ResponseEntity<UserResponse> getUserData(@PathVariable String userId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserData(Long.parseLong(userId)));
    }

    /**
     * Metodo che modifica i dati di uno specifico utente.
     * @param request DTO con tutti i nuovi dati da sovrascrivere.
     * @param userId Id dell'utente, passato in modo dinamico con l'endpoint.
     * @return DTO con i dati dell'utente modificati.
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
     * @param request DTO con tutti i nuovi dati da sovrascrivere.
     * @param username Username dell'utente da modificare, passato in modo dinamico con l'endpoint.
     * @return DTO con i dati dell'utente modificati.
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
     * Metodo per eliminare un utente dal database.
     * @param userId Id dell'utente, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di risposta al client.
     */
    @DeleteMapping("delete/{userId}")
    public ResponseEntity<MessageResponse> deleteAccount(@PathVariable String userId) {

        userService.deleteAccount(Long.parseLong(userId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Account eliminato con successo"));
    }

    /**
     * Dato un username in modo dinamico tramite l'endpoint, viene restituito al client l'utente con quell'username.
     * @param username Username dell'utente di cui si vogliono i dati.
     * @return DTO con i dati dell'utente richiesto.
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
    public ResponseEntity<MessageResponse> adminDeleteAccount(@PathVariable String username) {

        userService.adminDeleteAccount(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Account eliminato con successo"));
    }

    /**
     * Metodo per prendere tutti gli organizzatori presenti sul database.
     * @return Lista di DTO con i dati di ogni organizzatore.
     */
    @GetMapping("getAllOrganizzatori")
    public ResponseEntity<List<OrganizzatoreResponse>> getAllOrganizzatori() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllOrganizzatori());
    }

    /**
     * Metodo per prendere tutti gli utenti di un dato ruolo.
     * @param ruolo Ruolo richiesto, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli utenti richiesti.
     */
    @GetMapping("getAll/{ruolo}")
    public ResponseEntity<List<UserResponse>> getAllByRuolo(@PathVariable String ruolo) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllByRuolo(ruolo));
    }


    /**
     * Metodo che permette a un turista di seguire un organizzatore, e quindi essere notificati alla creazione di un evento.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di avvenuto follow.
     */
    @GetMapping("seguiOrganizzatore/{organizzatoreId}/{turistaId}")
    public ResponseEntity<MessageResponse> seguiOrganizzatore(
            @PathVariable String organizzatoreId,
            @PathVariable String turistaId) {

        userService.seguiOrganizzatore(Long.parseLong(organizzatoreId), Long.parseLong(turistaId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Organizzatore seguito con successo"));
    }

    /**
     * Metodo che, dato un turista, restituisce gli username degli organizzatori seguiti.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con gli username degli organizzatori.
     */
    @GetMapping("getOrganizzatoriSeguiti/{turistaId}")
    public ResponseEntity<List<OrganizzatoreSeguitoResponse>> getOrganizzatoriSeguiti(
            @PathVariable String turistaId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getOrganizzatoriSeguiti(Long.parseLong(turistaId)));
    }

    /**
     * Metodo che permette a un turista di smettere di seguire un organizzatore.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di avvenuto unfollow.
     */
    @GetMapping("smettiSeguireOrganizzatore/{organizzatoreId}/{turistaId}")
    public ResponseEntity<MessageResponse> smettiSeguireOrganizzatore(
            @PathVariable String organizzatoreId, @PathVariable String turistaId) {

        userService.smettiSeguireOrganizzatore(Long.parseLong(organizzatoreId), Long.parseLong(turistaId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Hai smesso di seguire l'organizzatore"));
    }
}
