package it.polimi.iswpf.service._interface;

import io.jsonwebtoken.Claims;
import it.polimi.iswpf.service.implementation.JwtServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link JwtServiceImpl}.
 */
public interface JwtService {

    /**
     * Metodo usato nel filter, semplicemente estrae l'username codificato nel jwt.
     * @param token stringa jwt.
     * @return username dell'utente in sessione.
     */
    String extractUsername(String token);

    /**
     * Dato il token e il tipo di dato SINGOLO che si vuole estrarre, viene ritornato proprio quel dato generico.
     * @param token stringa jwt.
     * @param claimsResolver funzione che riceve in ingresso i dati estratti dal token e ritorna il singolo dato richiesto.
     * @return il singolo dato richiesto codificato nel jwt.
     * @param <T> tipo del dato che voglio estrarre dal jwt.
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Codifica solamente dati dell'utente. Se bisogna codificare anche altri parametri,
     * viene usato il metodo sottostante a questo (overloading).
     * Quindi, il processo è descritto nel metodo sottostante a questo, l'unica differenza
     * è che se viene chiamato questo metodo allora l'HashMap sarà vuota.
     * @param userDetails dati dell'utente in sessione.
     * @return stringa jwt.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Tramite il builder, date le informazioni in ingresso viene codificato il tutto in un'unica stringa, ovvero il jwt.
     * @param extraClaims eventuali informazioni aggiuntive da codificare nel token.
     * @param userDetails dati dell'utente in sessione.
     * @return stringa jwt.
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Viene controllato se l'username estratto dal token è uguale a quello dell'utente in sessione
     * e poi la data di scadenza del token.
     * @param token stringa jwt.
     * @param userDetails dati dell'utente in sessione.
     * @return "true" se il token è valido, "false" se il token non è valido.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
