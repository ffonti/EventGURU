package it.polimi.iswpf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * Classe principale dell'applicazione. L'annotazione "@SpringBootApplication" combina le tre annotazioni
 * "@Configuration", "@EnableAutoConfiguration" e "@ComponentScan". Rappresenta l'unico punto di ingresso del backend.
 */
@SpringBootApplication
public class EventGURUApplication {

    private static Environment env;

    /**
     * Costruttore dell'applicazione.
     * @param env Istanza di {@link Environment} utilizzata per accedere alle proprietà del progetto.
     */
    public EventGURUApplication(Environment env) {

        EventGURUApplication.env = env;
    }

    /**
     * Metodo da eseguire per avviare l'intero progetto backend.
     * @param args Argomenti passati da riga di comando.
     */
    public static void main(String[] args) {

        SpringApplication.run(EventGURUApplication.class, args);
        //Messaggio sul log che notifica quando il server è pronto per l'uso, specificando la porta su cui lavora.
        System.out.println("Server listening on port " + env.getProperty("server.port"));
    }
}
