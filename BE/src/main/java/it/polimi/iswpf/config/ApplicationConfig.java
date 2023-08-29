package it.polimi.iswpf.config;

import it.polimi.iswpf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean utili per la configurazione generale dell'applicazione.
 */
@Configuration //Indica che verrà eseguito appena verrà avviato il server.
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Dato un username, viene preso l'utente dal database.
     * @return Dati dell'utente in sessione.
     */
    @Bean
    public UserDetailsService userDetailsService() {

        return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }

    /**
     * DAO che si occupa di prendere l'utente dal db, codificare la password, etc.
     * @return Un oggetto con le configurazioni settate.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        //Specifico il service da utilizzare per prendere i dati dell'utente dal db.
        authProvider.setUserDetailsService(userDetailsService());

        //Specifico il metodo per codificare la password.
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Specifica come gestire l'autenticazione.
     * @param config configurazione dell'authenticationManager.
     * @return Un'istanza dell'authenticationManager.
     * @throws Exception Eccezione generale che può essere lanciata durante la configurazione.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Specifico quale metodo utilizzare per codificare la password.
     * @return Un'istanza dell'encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
