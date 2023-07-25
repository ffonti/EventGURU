package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.exception.CampoVuotoException;
import it.polimi.iswpf.exception.RegistrazioneNonRiuscitaException;
import it.polimi.iswpf.exception.RuoloNonValidoException;
import it.polimi.iswpf.exception.UsernameRegistratoException;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service per gestire tutti i metodi inerenti all'autenticazione.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Metodo per la registrazione. Chiamando la repository, viene prima controllato se
     * esiste già un utente registrato con quell'username, poi viene controllato che ogni
     * campo non sia vuoto, poi viene salvato l'utente sul database e infine viene chiamato
     * di nuovo il database per controllare se l'utente è stato salvato correttamente.
     * @param request DTO con i dati per la registrazione -> {@link RegisterRequest }.
     * @throws Exception eccezione generale causata dal client.
     */
    @Override
    public void register(@NonNull RegisterRequest request) throws RuntimeException {

        //Controllo se è stata richiesta la registrazione di un admin, la quale non è permessa.
        if(request.getRuolo().equals(Ruolo.ADMIN)) {
            throw new RuoloNonValidoException();
        }

        //Controllo se esiste già un utente con questo username sul database.
        Optional<User> userAlreadyRegistered = userRepository.findByUsername(request.getUsername());
        if(userAlreadyRegistered.isPresent()) {
            throw new UsernameRegistratoException();
        }

        //Assegno a delle variabili i dati della richiesta.
        final String nome = request.getNome().trim();
        final String cognome = request.getCognome().trim();
        final String email = request.getEmail().trim().toLowerCase();
        final String username = request.getUsername().trim().toLowerCase();
        final String password = request.getPassword();
        final Ruolo ruolo = request.getRuolo();

        //Controllo che tutti i campi non siano vuoti.
        checkUserData(List.of(nome, cognome, email, username, password, ruolo.name()));

        //Costruisco l'oggetto utente con il design pattern builder
        User user = new UserBuilder()
                .nome(nome)
                .cognome(cognome)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .ruolo(ruolo)
                .iscrittoNewsletter(false)
                .build();

        userRepository.save(user); //Salvo l'utente sul database

        //Controllo se l'utente è presente sul database, se sì la registrazione è andata a buon fine.
        Optional<User> userRegistered = userRepository.findByUsername(username);
        if(userRegistered.isEmpty()) {
            throw new RegistrazioneNonRiuscitaException();
        }
    }

    /**
     * Metodo per il login. Viene chiamato l'authenticationManager a cui vengono passate
     * le credenziali per eseguire il login e gestirà anche le eccezioni. Successivamente
     * viene preso l'utente dal database con quell'username così da codificare i dati nel jwt.
     * @param request DTO con i dati per il login -> {@link LoginRequest }.
     * @return DTO con la stringa jwt -> {@link LoginResponse }.
     */
    @Override
    public String login(@NonNull LoginRequest request) {
        //Chiamo l'authenticationManager che si occuperà del login e delle eccezioni.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername().trim().toLowerCase(),
                    request.getPassword()
            )
        );

        //Prendo l'utente dal database.
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        //Codifico i dati dell'utente nel jwt.
        return jwtService.generateToken(user);
    }

    /**
     * Crea un oggetto {@link HttpHeaders} e aggiunge il token, così da mandarlo al client, come da prassi.
     * @param jwt Stringa con il token.
     * @return L'oggetto {@link HttpHeaders} con il token al suo interno.
     */
    @Override
    public HttpHeaders putJwtInHttpHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();

        /* Come da prassi, l'header viene chiamato "Authorization" e
        prima del jwt è presente la scritta "Bearer: " (tipo di token) */
        headers.add("Authorization", "Bearer: " + jwt);

        return headers;
    }

    /**
     * Controlla se tutti i campi sono stati compilati.
     * @param dataList Lista di stringhe da controllare.
     * @throws CampoVuotoException Eccezione causata da un campo vuoto.
     */
    @Override
    public void checkUserData(@NonNull List<String> dataList) throws CampoVuotoException {
        for(String data : dataList) {
            if(data.isEmpty() || data.isBlank()) {
                throw new CampoVuotoException();
            }
        }
    }
}