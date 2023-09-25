package it.polimi.iswpf.config;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configurazione dell'interazione tra client e server. {@link WebMvcConfigurer} è un'interfaccia
 * che viene utilizzata per configurare il framework Spring MVC in un'applicazione web.
 * Grazie all'annotazione "@Configuration", il contenuto di questa classe viene eseguito appena il server viene avviato.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configurazione delle richieste CORS dell'applicazione. Le richieste CORS sono
     * richieste HTTP che provengono da un dominio diverso da quello del server stesso.
     * @param registry Insieme delle regole di configurazione.
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {

        registry
            /* Specifica che tutte le richieste, indipendentemente dal
            path, devono essere gestite dalle successive regole. */
            .addMapping("/**")
            /* Imposta tutti i metodi HTTP (GET, POST, PUT, DELETE)
            come consentiti per le richieste CORS. */
            .allowedMethods("*");
    }
}