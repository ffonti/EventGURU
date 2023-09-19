package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.dto.response.OrganizzatoreResponse;
import it.polimi.iswpf.dto.response.UserResponse;
import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.Stato;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestire tutti i metodi inerenti all'user.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Metodo che, dato un id, prende l'utente dal database tramite la repository e lo ritorna al controller.
     * @param userId Id dell'utente.
     * @return DTO con tutti i dati dell'utente richiesto -> {@link UserResponse}.
     */
    @Override
    public UserResponse getUserData(Long userId) {

        //L'id autoincrement parte da 1.
        if(userId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'utente dal db con quell'id.
        Optional<User> userExists = userRepository.findByUserId(userId);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        //Se esiste, ritorno l'oggetto utente.
        return new UserResponse(
                userExists.get().getUserId(),
                userExists.get().getNome(),
                userExists.get().getCognome(),
                userExists.get().getEmail(),
                userExists.get().getUsername(),
                userExists.get().getPassword(),
                userExists.get().getRuolo(),
                userExists.get().isIscrittoNewsletter()
        );
    }

    /**
     * Metodo che, dato un id e un DTO con i nuovi dati, modifica i dati dell'utente con quell'id.
     * @param userId Id dell'utente.
     * @param request DTO con i nuovi dati {@link UpdateUserDataRequest}.
     * @return DTO con i dati dell'utente modificati -> {@link UserResponse}.
     */
    @Override
    public UserResponse updateUserData(Long userId, UpdateUserDataRequest request) {

        //L'id autoincrement parte da 1.
        if(userId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'utente dal db con quell'id.
        Optional<User> userExists = userRepository.findByUserId(userId);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        } else {
            //Se l'utente esiste, lo assegno a una variabile.
            User user = userExists.get();

            //Aggiorno i dati dell'utente.
            return updateGeneralUser(user, request);
        }
    }

    /**
     * Metodo che permette all'admin di modificare i dati di un utente, dato un username.
     * @param username Username dell'utente da modificare.
     * @param request DTO con i nuovi dati {@link UpdateUserDataRequest}.
     * @return DTO con i dati dell'utente modificati -> {@link UserResponse}.
     */
    @Override
    public UserResponse adminUpdateUserData(String username, UpdateUserDataRequest request) {

        //L'username dell'utente da modificare non può essere nullo
        if(username.isBlank() || username.isEmpty()) {
            throw new BadRequestException("Username non valido");
        }

        //Prendo l'utente dal db con quell'id.
        Optional<User> userExists = userRepository.findByUsername(username);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");

        } else {
            //Se l'utente esiste, lo assegno a una variabile.
            User user = userExists.get();

            //Aggiorno i dati dell'utente
            return updateGeneralUser(user, request);
        }
    }

    /**
     * Code cleaning. Metodo usato da due endpoint (admin e no) per modificare i dati di un utente.
     * @param user Utente da modificare.
     * @param request DTO con i nuovi dati -> {@link UpdateUserDataRequest}.
     * @return DTO con i dati dell'utente modificati -> {@link UserResponse}.
     */
    private UserResponse updateGeneralUser(User user, UpdateUserDataRequest request) {

        //Se il client ha compilato il campo "nome" e non è vuoto, aggiorno il nome dell'utente.
        if(!request.getNome().isEmpty() && !request.getNome().isBlank()) {
            user.setNome(request.getNome());
        }

        //Se il client ha compilato il campo "cognome" e non è vuoto, aggiorno il cognome dell'utente.
        if(!request.getCognome().isEmpty() && !request.getCognome().isBlank()) {
            user.setCognome(request.getCognome());
        }

        //Se il client ha compilato il campo "email" e non è vuoto, aggiorno l'email dell'utente.
        if(!request.getEmail().isEmpty() && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }

        //Se il client ha compilato il campo "username", non è vuoto e non è uguale a quello attuale,
        if(!request.getUsername().isEmpty() &&
                !request.getUsername().isBlank() &&
                !request.getUsername().equals(user.getUsername())) {

            //Controllo se esiste già un utente con il nuovo username.
            Optional<User> userWithUsername = userRepository.findByUsername(request.getUsername());

            //Se esiste, lancio un'eccezione.
            if(userWithUsername.isPresent()) {
                throw new ConflictException("Username già registrato");
            } else {
                //Se non esiste, assegno all'utente il nuovo username.
                user.setUsername(request.getUsername());
            }
        }

        //Se il client ha compilato i campi "vecchia password" e "nuova password" e non sono vuoti,
        if(!request.getNuovaPassword().isEmpty() &&
                !request.getNuovaPassword().isBlank() &&
                !request.getVecchiaPassword().isEmpty() &&
                !request.getVecchiaPassword().isBlank()) {

            //Controllo se la vecchia password è esatta, decodificandola. Se è errata lancio un'eccezione.
            if(!passwordEncoder.matches(request.getVecchiaPassword(), user.getPassword())) {
                throw new BadRequestException("Password errata");
            }

            //Se vecchia e nuova password sono uguali, lancio un'eccezione.
            if(request.getVecchiaPassword().equals(request.getNuovaPassword())) {
                throw new ConflictException("Le password sono uguali");
            }

            //Dopo aver passato i controlli, setto la nuova password codificata.
            user.setPassword(passwordEncoder.encode(request.getNuovaPassword()));
        }

        //Setto il booleano che indica se l'utente è iscritto alla newsletter.
        user.setIscrittoNewsletter(request.isIscrittoNewsletter());

        //Chiamo la repository e salvo i dati aggiornati dell'utente.
        userRepository.save(user);

        //Ritorno il DTO come risposta al client.
        return new UserResponse(
                user.getUserId(),
                user.getNome(),
                user.getCognome(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRuolo(),
                user.isIscrittoNewsletter()
        );
    }

    /**
     * Dato un ruolo ritorna una lista di utenti con quel ruolo.
     * @param ruolo Ruolo preso dall'endpoint.
     * @return Lista di DTO con i dati degli utenti con quel ruolo -> {@link UserResponse}.
     */
    @Override
    public List<UserResponse> getAll(String ruolo) {

        Optional<List<User>> utenti;

        //Chiamo la repository passando nella query il ruolo richiesto.
        if(ruolo.equals("TURISTA")) {
            utenti = userRepository.findAllByRuolo(Ruolo.TURISTA);
        } else if(ruolo.equals("ORGANIZZATORE")) {
            utenti = userRepository.findAllByRuolo(Ruolo.ORGANIZZATORE);
        } else {
            //Qualsiasi altro ruolo non è valido.
            throw new BadRequestException("Ruolo non valido");
        }

        //Se non è presente nessun utente lancio un'eccezione.
        if(utenti.isEmpty())  {
            throw new NotFoundException("Utenti non trovati");
        }

        //Se non sono presenti utenti, ritorno un array vuoto.
        List<UserResponse> response = new ArrayList<>();

        //Per ogni utente, aggiungo all'array di risposta i dati dell'utente.
        for(User user: utenti.get()) {
            response.add(new UserResponse(
                    user.getUserId(),
                    user.getNome(),
                    user.getCognome(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRuolo(),
                    user.isIscrittoNewsletter()
            ));
        }

        return response;
    }

    /**
     * Elimina l'utente dal database dopo aver fatto diversi controlli.
     * @param userId Id dell'utente da eliminare.
     */
    @Override
    public void deleteAccount(Long userId) {

        //L'id autoincrement parte da 1.
        if(userId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'utente dal db con quell'id.
        Optional<User> userExists = userRepository.findByUserId(userId);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        //Elimino l'utente dal database.
        userRepository.delete(userExists.get());

        //Controllo se esiste ancora l'utente con quell'id
        Optional<User> userDeleted = userRepository.findByUserId(userId);

        //Se non è stato eliminato, lancio un'eccezione.
        if(userDeleted.isPresent()) {
            throw new InternalServerErrorException("Errore nell'eliminazione dell'utente");
        }
    }

    /**
     * Dato un username, ed eseguiti diversi controlli, ritorno l'utente con quell'username.
     * @param username Username dell'utente richiesto.
     * @return DTO con i dati dell'utente richiesto -> {@link UserResponse}.
     */
    @Override
    public UserResponse getAdminUserData(String username) {

        //L'username dev'essere valido.
        if(username.isBlank() || username.isEmpty()) {
            throw new BadRequestException("Username non valido");
        }

        //Prendo l'utente dal db con quell'username.
        Optional<User> userExists = userRepository.findByUsername(username);

        //Se non esiste un utente con quell'username, lancio un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        //Ritorno il DTO con i dati dell'utente richiesto.
        return new UserResponse(
                userExists.get().getUserId(),
                userExists.get().getNome(),
                userExists.get().getCognome(),
                userExists.get().getEmail(),
                userExists.get().getUsername(),
                userExists.get().getPassword(),
                userExists.get().getRuolo(),
                userExists.get().isIscrittoNewsletter()
        );
    }

    /**
     * Dato un username, viene eliminato l'account.
     * @param username Username dell'account da eliminare.
     */
    @Override
    public void adminDeleteAccount(String username) {

        //L'username dev'essere valido.
        if(username.isBlank() || username.isEmpty()) {
            throw new BadRequestException("Username non valido");
        }

        //Prendo l'utente dal db con quell'username.
        Optional<User> userExists = userRepository.findByUsername(username);

        //Se non esiste un utente con quell'username, lancio un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        //Elimino l'utente dal database.
        userRepository.delete(userExists.get());

        //Controllo se esiste ancora l'utente con quell'id
        Optional<User> userDeleted = userRepository.findByUsername(username);

        //Se non è stato eliminato, lancio un'eccezione.
        if(userDeleted.isPresent()) {
            throw new InternalServerErrorException("Errore nell'eliminazione dell'utente");
        }
    }

    /**
     * Metodo per prendere tutti gli organizzatori presenti sul database.
     * @return Lista di DTO con i dati di ogni organizzatore -> {@link OrganizzatoreResponse}.
     */
    @Override
    public List<OrganizzatoreResponse> getAllOrganizzatori() {

        Optional<List<User>> organizzatori = userRepository.findAllByRuolo(Ruolo.ORGANIZZATORE);

        //Se non è presente nessun utente lancio un'eccezione.
        if(organizzatori.isEmpty()) {
            throw new NotFoundException("Organizzatori non trovati");
        }

        //Inizializzo la variabile di risposta
        List<OrganizzatoreResponse> response = new ArrayList<>();

        //Per ogni utente, aggiungo all'array di risposta i dati dell'utente.
        for(User organizzatore: organizzatori.get()) {
            response.add(new OrganizzatoreResponse(
                organizzatore.getUserId(),
                organizzatore.getNome(),
                organizzatore.getCognome(),
                organizzatore.getDataCreazione(),
                organizzatore.getEventi().stream()
                        .filter(evento -> evento.getDataInizio().isAfter(LocalDateTime.now()))
                        .count()
            ));
        }

        return response;
    }
}
