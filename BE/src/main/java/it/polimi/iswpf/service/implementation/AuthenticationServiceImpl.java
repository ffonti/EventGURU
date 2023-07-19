package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.exception.ApiRequestException;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    public void register(@NonNull RegisterRequest request) throws Exception {

        final String nome = request.getNome().trim();
        final String cognome = request.getCognome().trim();
        final String email = request.getEmail().trim().toLowerCase();
        final String username = request.getUsername().trim().toLowerCase();
        final String password = request.getPassword();

        //Controllo se esiste già un utente con questo username sul database.
        Optional<User> userAlreadyRegistered = userRepository.findByUsername(username);
        if(userAlreadyRegistered.isPresent()) {
            throw new ApiRequestException("Username già registrato");
        }

        //Controllo che tutti i campi non siano vuoti.
        checkUserData(List.of(nome, cognome, email, username, password));

        //Creo un'istanza di user, tramite il builder implementato da zero.
        User user = new UserBuilder()
                .nome(nome)
                .cognome(cognome)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .ruolo(Ruolo.USER)
                .iscrittoNewsletter(false)
                .build();

        //Salvo l'utente sul database.
        userRepository.save(user);

        //Controllo se il salvataggio è andato a buon fine.
        Optional<User> userRegistered = userRepository.findByUsername(username);
        if(userRegistered.isEmpty()) {
            throw new ApiRequestException("Errore nella registrazione");
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
    public LoginResponse login(@NonNull LoginRequest request) {
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
        String jwt = jwtService.generateToken(user);

        return new LoginResponse(jwt);
    }

    /**
     * Per ogni elemento della lista, viene controllato che non sia vuoto.
     * @param dataList lista con i campi inseriti dal client.
     * @throws Exception eccezione causata dal campo vuoto.
     */
    public void checkUserData(@NonNull List<String> dataList) throws Exception {
        for(String data : dataList) {
            if(data.isEmpty() || data.isBlank()) {
                throw new ApiRequestException("Inserire tutti i campi");
            }
        }
    }
}