package it.polimi.iswpf.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.polimi.iswpf.service._interface.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service per gestire tutti i metodi inerenti al jwt.
 */
@Service
public class JwtServiceImpl implements JwtService {

    //Stringa che elabora una chiave segreta per codificare e decodificare il token.
    private static final String SECRET_KEY = "4D30457130736F38466A626A6251534F6D61366E31674F7356394B395031676A";

    /**
     * Metodo usato nel filter, semplicemente estrae l'username codificato nel jwt.
     * @param token stringa jwt.
     * @return username dell'utente in sessione.
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); //getSubject restituisce l'attributo univoco dell'utente.
    }

    /**
     * Dato il token e il tipo di dato SINGOLO che si vuole estrarre, viene ritornato proprio quel dato generico.
     * @param token stringa jwt.
     * @param claimsResolver funzione che riceve in ingresso i dati estratti dal token e ritorna il singolo dato richiesto.
     * @return il singolo dato richiesto codificato nel jwt.
     * @param <T> tipo del dato che voglio estrarre dal jwt.
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); //Estraggo tutti i dati codificati nel token.
        //Estraggo il singolo dato (generico) dall'insieme di tutti i dati codificati nel token.
        return claimsResolver.apply(claims);
    }

    /**
     * Codifica solamente dati dell'utente. Se bisogna codificare anche altri parametri,
     * viene usato il metodo sottostante a questo (overloading).
     * Quindi, il processo è descritto nel metodo sottostante a questo, l'unica differenza
     * è che se viene chiamato questo metodo allora l'HashMap sarà vuota.
     * @param userDetails dati dell'utente in sessione.
     * @return stringa jwt.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Tramite il builder, date le informazioni in ingresso viene codificato il tutto in un'unica stringa, ovvero il jwt.
     * @param extraClaims eventuali informazioni aggiuntive da codificare nel token.
     * @param userDetails dati dell'utente in sessione.
     * @return stringa jwt.
     */
    @Override
    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        return Jwts
            .builder()
            .setClaims(extraClaims) //Aggiungo eventuali informazioni aggiuntive da codificare.
            .setSubject(userDetails.getUsername()) //Aggiungo l'attributo univoco dell'utente.
            .setIssuedAt(new Date(System.currentTimeMillis())) //Data di creazione del token.
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) //Data di scadenza del token.
            .signWith(getSigninKey(), SignatureAlgorithm.HS256) //Codifico con la chiave segreta e specifico l'algoritmo.
            .compact(); //Genera il token
    }

    /**
     * Viene controllato se l'username estratto dal token è uguale a quello dell'utente in sessione
     * e poi la data di scadenza del token.
     * @param token stringa jwt.
     * @param userDetails dati dell'utente in sessione.
     * @return "true" se il token è valido, "false" se il token non è valido.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return ((username.equals(userDetails.getUsername())) && !isTokenExpired(token));
    }

    /**
     *  Controlla se la data di scadenza del token è precedente a quella attuale.
     * @param token stringa jwt.
     * @return "true" se il token è scaduto, "false" se il token non è scaduto.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Dato un token, viene restituita la data di scadenza.
     * @param token stringa jwt.
     * @return la data di scadenza del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Utilizza la classe Jwts della dependency io.jsonwebtoken per
     * estrarre i dati che sono stati codificati nel jwt.
     * @param token stringa jwt.
     * @return tutti i dati codificati nel jwt.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() //Parse per poter utilizzare il builder.
                .setSigningKey(getSigninKey()) //Setto la chiave segreta per codificare e decodificare il token.
                .build() //Applico il pattern builder, quindi "costruisco" l'oggetto.
                .parseClaimsJws(token) //Mando il token da decodificare.
                .getBody(); //Prendo tutti i dati codificati nel jwt.
    }

    /**
     * Decodifica la stringa in base 64 e la codifica secondo un algoritmo di hash, così da poter lavorare con il jwt.
     * @return la chiave codificata, con cui gestire il jwt.
     */
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); //Decodifico la chiave secreta in base 64.
        /* Viene codificata la chiave secondo un tipo di algoritmo di hash a chiave costruito a partire
        dalla funzione di hash SHA-256 e utilizzato come Hash-based Message Authentication Code (HMAC). */
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
