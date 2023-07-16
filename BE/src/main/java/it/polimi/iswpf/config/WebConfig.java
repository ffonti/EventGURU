package it.polimi.iswpf.config;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configurazione dell'interazione tra client e server. {@link WebMvcConfigurer} è un'interfaccia
 * che viene utilizzata per configurare il framework Spring MVC in un'applicazione web.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configurazione delle richieste CORS nell'applicazione. Le richieste CORS sono
     * richieste HTTP che provengono da un dominio diverso da quello del server stesso.
     * addMapping("/**") specifica che tutte le richieste, indipendentemente dal
     * path, devono essere gestite dalle successive regole.
     * allowedMethods("*") imposta tutti i metodi HTTP (GET, POST, PUT, DELETE) come
     * consentiti per le richieste CORS.
     * @param registry insieme delle regole di configurazione.
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}