package it.polimi.iswpf.security.filter;

import it.polimi.iswpf.security.service.implementation.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Questa classe contiene metodi che vengono eseguiti ogni volta che arriva una richiesta al server.
 * Per configurarla secondo quanto detto, deve estendere la classe OncePerRequestFilter.
 * (Un'altra soluzione è quella d'implementare direttamente l'interfaccia Filter).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Questo metodo funziona esattamente come il doFilter classico, ma qui abbiamo la sicurezza che verrà
     * eseguito una sola volta per ogni richiesta all'interno.
     * @param request Effettiva richiesta del client dalla quale possiamo estrarre i dati.
     * @param response Effettiva risposta del server che può contenere eventuali dati.
     * @param filterChain Contiene la lista dei filtri da eseguire. Si tratta del Chain of Responsibility pattern.
     * @throws ServletException Generata quando vi è un errore generico su una servlet.
     * @throws IOException Generata quando si va incontro a problemi di operazioni I/O.
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); //Prendo il jwt dall'header della richiesta.
        final String jwt;
        final String username;

        //Se c'è un errore con l'authorization header, continuo con la catena di filtri ed esco dal metodo.
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); //Passo request e response al prossimo filtro.
            return;
        }

        jwt = authHeader.substring("Bearer ".length()); //Estraggo l'effettivo jwt dall'header.
        username = jwtService.extractUsername(jwt); //Estraggo l'username codificato nel jwt.

        /* Se l'attributo univoco esiste e l'utente non è autenticato,
        controllo se ho un utente sul db con questo username. */
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Prendo dal db i dati dell'utente con quell'username.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            //Controllo se il token è valido.
            if(jwtService.isTokenValid(jwt, userDetails)) {
                //Aggiorno il token con i dati dell'utente.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                //Setto i dati da codificare.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Sovrascrivo il token in sessione.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Continuo con la catena di filtri da eseguire.
        filterChain.doFilter(request, response);
    }
}
