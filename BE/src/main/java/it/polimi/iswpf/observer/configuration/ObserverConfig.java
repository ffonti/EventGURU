package it.polimi.iswpf.observer.configuration;

import it.polimi.iswpf.model.EventType;
import it.polimi.iswpf.observer.listener.FollowersListener;
import it.polimi.iswpf.observer.listener.NewsletterListener;
import it.polimi.iswpf.observer.publisher.EventManager;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe di configurazione dell'Observer eseguita all'avvio dell'applicazione in modo da effettuare le subscribe.
 * Grazie all'annotazione "@Configuration", il contenuto di questa classe viene eseguito appena il server viene avviato.
 */
@Configuration
@RequiredArgsConstructor
public class ObserverConfig {

    private final FollowersListener followersListener;
    private final NewsletterListener newsletterListener;

    /**
     * Metodo che inizializza l'Observer facendo le subscribe per ogni implementazione del listener.
     */
    @Bean
    public void initObserver() {

        System.out.println("Subscribing...");

        EventManager.getInstance().subscribe(EventType.FOLLOWERS, followersListener);
        EventManager.getInstance().subscribe(EventType.NEWSLETTER, newsletterListener);

        System.out.println("Subscribe completed");
    }

    /**
     * L'annotation @PreDestroy esegue il metodo subito prima del termine dell'applicazione, in modo da effettuare tutti gli unsubscribe.
     */
    @PreDestroy
    public void onDestroy() {

        System.out.println("Unsubscribing...");

        EventManager.getInstance().unsubscribe(EventType.FOLLOWERS);
        EventManager.getInstance().unsubscribe(EventType.NEWSLETTER);

        System.out.println("Unsubscribe completed");
    }
}
