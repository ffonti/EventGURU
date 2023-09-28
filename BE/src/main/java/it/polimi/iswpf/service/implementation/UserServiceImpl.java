package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.dto.response.OrganizzatoreResponse;
import it.polimi.iswpf.dto.response.OrganizzatoreSeguitoResponse;
import it.polimi.iswpf.dto.response.UserResponse;
import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.model.Ruolo;
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
     * @return DTO con tutti i dati dell'utente.
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
     * Metodo che, modifica i dati dell'utente con quell'id.
     * @param userId Id dell'utente.
     * @param request DTO con i nuovi dati.
     * @return DTO con i dati dell'utente aggiornati.
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
        }

        //Se l'utente esiste, lo assegno a una variabile.
        User user = userExists.get();

        //Aggiorno i dati dell'utente.
        return updateGeneralUser(user, request);
    }

    /**
     * Metodo che permette all'admin di modificare i dati di un utente, dato un username.
     * @param username Username dell'utente da modificare.
     * @param request DTO con i nuovi dati.
     * @return DTO con i dati dell'utente aggiornati.
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

        }

        //Se l'utente esiste, lo assegno a una variabile.
        User user = userExists.get();

        //Aggiorno i dati dell'utente
        return updateGeneralUser(user, request);
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
     * @return DTO con i dati dell'utente richiesto.
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

        //Controllo se esiste ancora l'utente con quell'id.
        Optional<User> userDeleted = userRepository.findByUsername(username);

        //Se non è stato eliminato, lancio un'eccezione.
        if(userDeleted.isPresent()) {
            throw new InternalServerErrorException("Errore durante l'eliminazione dell'utente");
        }
    }

    /**
     * Metodo per prendere tutti gli organizzatori presenti sul database.
     * @return Lista di DTO con i dati di ogni organizzatore.
     */
    @Override
    public List<OrganizzatoreResponse> getAllOrganizzatori() {

        //Prendo dal db tutti gli organizzatori.
        Optional<List<User>> organizzatori = userRepository.findAllByRuolo(Ruolo.ORGANIZZATORE);

        //Se non è presente nessun utente lancio un'eccezione.
        if(organizzatori.isEmpty()) {
            throw new NotFoundException("Organizzatori non trovati");
        }

        //Inizializzo la variabile di risposta.
        List<OrganizzatoreResponse> response = new ArrayList<>();

        //Per ogni utente, aggiungo all'array di risposta i dati dell'utente.
        for(User organizzatore: organizzatori.get()) {
            response.add(new OrganizzatoreResponse(
                organizzatore.getUserId(),
                organizzatore.getUsername(),
                organizzatore.getNome(),
                organizzatore.getCognome(),
                organizzatore.getDataCreazione(),
                //Prendo il numero di eventi futuri organizzati dall'organizzatore.
                organizzatore.getEventi().stream()
                        .filter(evento -> evento.getDataInizio().isAfter(LocalDateTime.now()))
                        .count()
            ));
        }

        return response;
    }

    /**
     * Metodo per prendere tutti gli utenti di un dato ruolo.
     * @param ruolo Ruolo richiesto, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli utenti richiesti.
     */
    @Override
    public List<UserResponse> getAllByRuolo(String ruolo) {

        //Controllo la validità del campo.
        if(ruolo.isBlank() || ruolo.isEmpty()) {
            throw new BadRequestException("Ruolo non valido");
        }

        //In base al ruolo richiesto dal client, creo una variabile da usare per chiamare il db.
        final Ruolo ruoloDaCercare = switch (ruolo) {
            case "TURISTA" -> Ruolo.TURISTA;
            case "ORGANIZZATORE" -> Ruolo.ORGANIZZATORE;
            default -> throw new BadRequestException("Ruolo non valido");
        };

        //Prendo dal db tutti gli utenti.
        Optional<List<User>> users = userRepository.findAllByRuolo(ruoloDaCercare);

        //Se non è presente nessun utente lancio un'eccezione.
        if(users.isEmpty()) {
            throw new NotFoundException("Utenti non trovati");
        }

        //Inizializzo la variabile di risposta.
        List<UserResponse> response = new ArrayList<>();

        //Per ogni utente, aggiungo all'array di risposta i dati.
        for(User user: users.get()) {
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
     * Metodo che permette a un turista di seguire un organizzatore, e quindi essere notificati alla creazione di un evento.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     */
    @Override
    public void seguiOrganizzatore(Long organizzatoreId, Long turistaId) {

        //L'id autoincrement parte da 1.
        if(organizzatoreId < 1 || turistaId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo gli utenti dal db con quegli id.
        Optional<User> organizzatoreExists = userRepository.findByUserId(organizzatoreId);
        Optional<User> turistaExists = userRepository.findByUserId(turistaId);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(organizzatoreExists.isEmpty() || turistaExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        User organizzatore = organizzatoreExists.get();
        User turista = turistaExists.get();

        //Controllo il ruolo di turista e organizzatore.
        if(!organizzatore.getRuolo().equals(Ruolo.ORGANIZZATORE) || !turista.getRuolo().equals(Ruolo.TURISTA)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Controllo che il turista non segua già l'organizzatore.
        if(turista.getSeguiti().contains(organizzatore)) {
            throw new BadRequestException("Il turista segue già l'organizzatore");
        }

        //Aggiungo l'organizzatore alla lista dei seguiti del turista.
        turista.getSeguiti().add(organizzatore);

        //Aggiorno i dati sul database.
        userRepository.save(turista);
    }

    /**
     * Metodo che, dato un turista, restituisce gli username degli organizzatori seguiti.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con gli username degli organizzatori.
     */
    @Override
    public List<OrganizzatoreSeguitoResponse> getOrganizzatoriSeguiti(Long turistaId) {

        //L'id autoincrement parte da 1.
        if(turistaId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'utente dal db con quell'id.
        Optional<User> turistaExists = userRepository.findByUserId(turistaId);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(turistaExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        //Controllo il ruolo di turista.
        if(!turistaExists.get().getRuolo().equals(Ruolo.TURISTA)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Inizializzo l'array di risposta.
        List<OrganizzatoreSeguitoResponse> response = new ArrayList<>();

        //Aggiungo i dati al DTO.
        for(User organizzatoreSeguito : turistaExists.get().getSeguiti()) {
            response.add(new OrganizzatoreSeguitoResponse(organizzatoreSeguito.getUsername()));
        }

        //Ritorno il DTO.
        return response;
    }

    /**
     * Metodo che permette a un turista di smettere di seguire un organizzatore.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     */
    @Override
    public void smettiSeguireOrganizzatore(Long organizzatoreId, Long turistaId) {

        //L'id autoincrement parte da 1.
        if(organizzatoreId < 1 || turistaId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo gli utenti dal db con quegli id.
        Optional<User> organizzatoreExists = userRepository.findByUserId(organizzatoreId);
        Optional<User> turistaExists = userRepository.findByUserId(turistaId);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(organizzatoreExists.isEmpty() || turistaExists.isEmpty()) {
            throw new NotFoundException("Utente non trovato");
        }

        User organizzatore = organizzatoreExists.get();
        User turista = turistaExists.get();

        //Controllo il ruolo di turista e organizzatore.
        if(!organizzatore.getRuolo().equals(Ruolo.ORGANIZZATORE) || !turista.getRuolo().equals(Ruolo.TURISTA)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Controllo che il turista segua l'organizzatore.
        if(!turista.getSeguiti().contains(organizzatore)) {
            throw new BadRequestException("Il turista non segue l'organizzatore");
        }

        //Aggiungo l'organizzatore alla lista dei seguiti del turista.
        turista.getSeguiti().remove(organizzatore);

        //Aggiorno i dati sul database.
        userRepository.save(turista);
    }

    /**
     * Code cleaning. Metodo usato da due endpoint (admin e no) per modificare i dati di un utente.
     * @param user Utente da modificare.
     * @param request DTO con i nuovi dati.
     * @return DTO con i dati dell'utente modificati.
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
}
