package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.model._enum.Ruolo;
import it.polimi.iswpf.model.entity.User;
import it.polimi.iswpf.model.repository.UserRepository;
import it.polimi.iswpf.security.service.implementation.JwtServiceImpl;
import it.polimi.iswpf.service._interface.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * Metodo per registrare un utente sul database.
     * @param request DTO con i dati per la registrazione.
     * @throws RuntimeException possibile eccezione causata dal client.
     */
    @Override
    public void register(@NonNull RegisterRequest request) throws RuntimeException {

        //Controllo se è stata richiesta la registrazione di un admin, la quale non è permessa.
        if(request.getRuolo().equals("ADMIN")) {
            throw new BadRequestException("Ruolo non valido");
        }

        //Controllo se esiste già un utente con questo username sul database.
        Optional<User> userAlreadyRegistered = userRepository.findByUsername(request.getUsername());

        //Se esiste, lancio un eccezione.
        if(userAlreadyRegistered.isPresent()) {
            throw new ConflictException("Username già registrato");
        }

        //Assegno a delle variabili i dati della richiesta.
        final String nome = request.getNome().trim();
        final String cognome = request.getCognome().trim();
        final String email = request.getEmail().trim().toLowerCase();
        final String username = request.getUsername().trim().toLowerCase();
        final String password = request.getPassword();
        final Ruolo ruolo = switch (request.getRuolo()) {
            case "TURISTA" -> Ruolo.TURISTA;
            case "ORGANIZZATORE" -> Ruolo.ORGANIZZATORE;
            default -> throw new BadRequestException("Ruolo non valido");
        };

        //Controllo che tutti i campi non siano vuoti.
        checkUserData(List.of(nome, cognome, email, username, password, ruolo.name()));

        //Costruisco l'oggetto utente con il pattern builder
        User user = new UserBuilder()
                .nome(nome)
                .cognome(cognome)
                .email(email)
                .username(username)
                //Password codificata.
                .password(passwordEncoder.encode(password))
                .ruolo(ruolo)
                .iscrittoNewsletter(false)
                .dataCreazione(LocalDateTime.now())
                .build();

        //Salvo l'utente sul database
        userRepository.save(user);

        //Controllo se l'utente è presente sul database, se sì la registrazione è andata a buon fine.
        Optional<User> userRegistered = userRepository.findByUsername(username);

        //Se l'utente appena registrato non è presente sul database, lancio un'eccezione.
        if(userRegistered.isEmpty()) {
            throw new InternalServerErrorException("Registrazione non riuscita");
        }
    }

    /**
     * Metodo per il login. Dopo aver eseguito i controlli viene generato un jwt per l'utente loggato.
     * @param request DTO con i dati per il login.
     * @return DTO con i dati dell'utente.
     */
    @Override
    public LoginResponse login(@NonNull LoginRequest request) {

        //Chiamo l'authenticationManager che si occuperà del login e delle eccezioni.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername().trim().toLowerCase(),
                    request.getPassword()
            )
        );

        //Prendo l'utente appena loggato dal database.
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        //Costruisco il DTO con i dati dell'utente e lo ritorno.
        return new LoginResponse(
                user.getUserId(),
                user.getNome(),
                user.getCognome(),
                user.getUsername(),
                user.getRuolo(),
                user.getEmail(),
                user.isIscrittoNewsletter(),
                "Accesso eseguito!",
                jwtService.generateToken(user)
        );
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
     * @throws RuntimeException Eccezione causata da un campo vuoto.
     */
    private void checkUserData(@NonNull List<String> dataList) throws RuntimeException {

        //Scorro tutta la lista passata come parametro.
        for(String data : dataList) {
            if(data.isEmpty() || data.isBlank()) {
                throw new BadRequestException("Compilare tutti i campi");
            }
        }
    }
}