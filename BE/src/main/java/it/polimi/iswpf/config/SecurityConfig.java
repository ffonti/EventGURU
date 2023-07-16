package it.polimi.iswpf.config;

import it.polimi.iswpf.filter.JwtAuthenticationFilter;
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
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configurazione della catena di filtri per la sicurezza http.
     * @param http contiene tutte le configurazioni per la sicurezza.
     * @return l'oggetto HttpSecurity configurato.
     * @throws Exception eccezione generale a cui si può andare incontro.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf() //Controlla se la richiesta è stata inviata intenzionalmente o meno.
            .disable() //Disabilita le due configurazioni precedenti.
            .authorizeHttpRequests() //Autorizza le richieste http.
            .requestMatchers("/api/v1/auth/**") //Whitelist.
            .permitAll() //Permette tutte le operazioni alla whitelist.
            .anyRequest() //Per tutte le altre richieste.
            .authenticated() //Deve essere eseguita l'autenticazione.
            .and()
            .sessionManagement() //Gestisco la sessione.
                //Spring crea una nuova sessione per ogni richiesta (stateless).
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                //Seleziono authentication provider adatto.
            .authenticationProvider(authenticationProvider)
                //Viene eseguito il controllo sul jwt.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
