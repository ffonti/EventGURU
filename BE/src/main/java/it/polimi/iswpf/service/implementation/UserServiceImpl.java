package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * @return Oggetto {@link User} con tutti i dati dell'utente.
     */
    @Override
    public User getUserData(Long userId) {

        //L'id autoincrement parte da 1.
        if(userId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'utente dal db con quell'id.
        Optional<User> userExists = userRepository.findByUserId(userId);

        //Se non esiste un utente con quell'id, lanco un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        //Se esiste, ritorno l'oggetto utente.
        return userExists.get();
    }

    /**
     * Metodo che, dato un id e un DTO con i nuovi dati, modifica i dati dell'utente con quell'id.
     * @param userId Id dell'utente.
     * @param request DTO con i nuovi dati {@link UpdateUserDataRequest}.
     * @return L'oggetto utente con i dati aggiornati.
     */
    @Override
    public User updateUserData(Long userId, UpdateUserDataRequest request) {

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

            //Ritorno l'utente come risposta al client.
            return user;
        }
    }

    /**
     * Dato un ruolo ritorna una lista di utenti con quel ruolo.
     * @param ruolo Ruolo preso dall'endpoint.
     * @return Lista di utenti presa dal db.
     */
    @Override
    public List<User> getAll(String ruolo) {

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

        //Ritorno la lista di utenti con quel ruolo.
        return utenti.get();
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
}
