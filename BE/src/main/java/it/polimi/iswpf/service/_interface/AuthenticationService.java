package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.service.implementation.AuthenticationServiceImpl;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;

/**
 * Interfaccia che contiene le firme dei metodi del service che gestisce l'autenticazione.
 * Implementazione -> {@link AuthenticationServiceImpl}.
 */
public interface AuthenticationService {

    /**
     * Metodo per registrare un utente sul database.
     * @param request DTO con i dati per la registrazione.
     * @throws RuntimeException possibile eccezione causata dal client.
     */
    void register(@NonNull RegisterRequest request) throws RuntimeException;

    /**
     * Metodo per il login. Dopo aver eseguito i controlli viene generato un jwt per l'utente loggato.
     * @param request DTO con i dati per il login.
     * @return DTO con i dati dell'utente.
     */
    LoginResponse login(@NonNull LoginRequest request);

    /**
     * Crea un oggetto {@link HttpHeaders} e aggiunge il token, così da mandarlo al client, come da prassi.
     * @param jwt Stringa con il token.
     * @return L'oggetto {@link HttpHeaders} con il token al suo interno.
     */
    HttpHeaders putJwtInHttpHeaders(String jwt);
}
