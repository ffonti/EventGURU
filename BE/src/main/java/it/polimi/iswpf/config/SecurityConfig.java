package it.polimi.iswpf.config;

import it.polimi.iswpf.filter.JwtAuthenticationFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configurazione base di spring security.
 */
@Configuration //Indica che verrà eseguito appena verrà avviato il server.
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configurazione della catena di filtri per la sicurezza http.
     * @param http Contiene tutte le configurazioni per la sicurezza.
     * @return L'oggetto HttpSecurity configurato.
     * @throws Exception Eccezione generale causata dalla catena di filtri.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(@NonNull HttpSecurity http) throws Exception {

        http
            .cors() //Controlla la provenienza del client.
            .and()
            .csrf() //Controlla se la richiesta è stata inviata intenzionalmente o meno.
            .disable() //Disabilita le due impostazioni precedenti.
            .authorizeHttpRequests() //Autorizza le richieste http.
            .requestMatchers("/api/v1/auth/**") //Whitelist.
            .permitAll() //Permette tutte le operazioni alla whitelist.
            .anyRequest() //Per tutte le altre richieste.
            .authenticated() //Deve essere eseguita l'autenticazione.
            .and()
            .sessionManagement() //Viene abilitato il sistema di sessioni.
                //Spring crea una nuova sessione per ogni richiesta (stateless).
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                //Sistema di autenticazione offerto da Spring Security.
            .authenticationProvider(authenticationProvider)
                //Viene eseguito il controllo sul jwt.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
