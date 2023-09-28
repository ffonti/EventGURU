package it.polimi.iswpf.service._interface;

import io.jsonwebtoken.Claims;
import it.polimi.iswpf.service.implementation.JwtServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

/**
 * Interfaccia che contiene le firme dei metodi del service che gestisce i jwt.
 * Implementazione -> {@link JwtServiceImpl}.
 */
public interface JwtService {

    /**
     * Metodo usato nel filter, semplicemente estrae l'username codificato nel jwt.
     * @param token Stringa jwt.
     * @return Username dell'utente estratto dal jwt.
     */
    String extractUsername(String token);

    /**
     * Dato il token e il tipo di dato SINGOLO che si vuole estrarre, viene ritornato proprio quel dato generico.
     * @param token Stringa jwt.
     * @param claimsResolver Funzione che riceve in ingresso i dati estratti dal token e ritorna il singolo dato richiesto.
     * @return Il singolo dato richiesto codificato nel jwt.
     * @param <T> Tipo del dato che voglio estrarre dal jwt.
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Codifica nel token solamente dati dell'utente. Se bisogna codificare anche altri parametri,
     * viene usato il metodo sottostante a questo (overloading).
     * @return Stringa jwt.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Date le informazioni in ingresso viene codificato il tutto nel jwt.
     * @param extraClaims Eventuali informazioni aggiuntive da codificare nel token.
     * @param userDetails Dati dell'utente in sessione.
     * @return Stringa jwt.
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Viene controllato se l'username estratto dal token è uguale a quello dell'utente in sessione
     * e poi la data di scadenza del token.
     * @param token Stringa jwt.
     * @param userDetails Dati dell'utente in sessione.
     * @return "true" se il token è valido, "false" se il token non è valido.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
