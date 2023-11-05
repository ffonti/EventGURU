package it.polimi.iswpf.security.config;

import it.polimi.iswpf.security.filter.JwtAuthenticationFilter;
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
 * Configurazione di spring security. Grazie all'annotazione "@Configuration",
 * il contenuto di questa classe viene eseguito appena il server viene avviato.
 */
@Configuration
@EnableWebSecurity //Utilizzata per abilitare la sicurezza web nell'applicazione.
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Bean che contiene la configurazione della catena di filtri per la sicurezza http.
     * @param http Istanza di {@link HttpSecurity} con il quale settare le configurazioni di sicurezza.
     * @return Istanza configurata.
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
            .anyRequest() //Per tutte le altre richieste invece,
            .authenticated() //deve essere eseguita l'autenticazione.
            .and()
            .sessionManagement() //Viene abilitato il sistema di sessioni.
            // Spring crea una nuova sessione per ogni richiesta (stateless).
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            //Sistema di autenticazione offerto da Spring Security.
            .authenticationProvider(authenticationProvider)
            //Viene eseguito il controllo sul jwt.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
