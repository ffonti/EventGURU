package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.exception.CampoVuotoException;
import it.polimi.iswpf.service.implementation.AuthenticationServiceImpl;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link AuthenticationServiceImpl}.
 */
public interface AuthenticationService {

    /**
     * Metodo per la registrazione. Chiamando la repository, viene prima controllato se
     * esiste già un utente registrato con quell'username, poi viene controllato che ogni
     * campo non sia vuoto, poi viene salvato l'utente sul database e infine viene chiamato
     * di nuovo il database per controllare se l'utente è stato salvato correttamente.
     * @param request DTO con i dati per la registrazione -> {@link RegisterRequest }.
     * @throws Exception eccezione generale causata dal client.
     */
    void register(@NonNull RegisterRequest request) throws RuntimeException;

    /**
     * Metodo per il login. Viene chiamato l'authenticationManager a cui vengono passate
     * le credenziali per eseguire il login e gestirà anche le eccezioni. Successivamente
     * viene preso l'utente dal database con quell'username così da codificare i dati nel jwt.
     * @param request DTO con i dati per il login -> {@link LoginRequest }.
     * @return DTO con la stringa jwt -> {@link LoginResponse }.
     */
    String login(@NonNull LoginRequest request);

    /**
     * Crea un oggetto {@link HttpHeaders} e aggiunge il token, così da mandarlo al client, come da prassi.
     * @param jwt Stringa con il token.
     * @return L'oggetto {@link HttpHeaders} con il token al suo interno.
     */
    HttpHeaders putJwtInHttpHeaders(String jwt);

    /**
     * Controlla se tutti i campi sono stati compilati.
     * @param dataList Lista di stringhe da controllare.
     * @throws CampoVuotoException Eccezione causata da un campo vuoto.
     */
    void checkUserData(@NonNull List<String> dataList) throws RuntimeException;
}
