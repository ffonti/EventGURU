package it.polimi.iswpf.observer.publisher;

import it.polimi.iswpf.model.EventType;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.observer.listener.EventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Publisher del pattern Observer, implementato tramite il Singleton. Questa classe espone metodi utili per
 * entrambi i pattern: subscribe, unsubscribe e notify per la gestione dei listeners dell'Observer;
 * getInstance e il costruttore per la gestione del Singleton.
 */
public class EventManager {

    //Unica istanza statica della classe.
    private static EventManager instance;
    //HashMap per tutte le implementazioni dei listeners.
    private Map<EventType, EventListener> listeners = new HashMap<>();

    /**
     * Costruttore vuoto per il Singleton pattern.
     */
    private EventManager() { }

    /**
     * Metodo del Singleton pattern per prendere sempre la stessa istanza e non crearne una nuova.
     * @return L'unica istanza della classe.
     */
    public static EventManager getInstance() {

        //Se non esiste ancora un'istanza, la crea.
        if(instance == null) {
            instance = new EventManager();
        }

        return instance;
    }

    /**
     * Metodo per iscrivere un listener al publisher.
     * @param eventType Tipo dell'evento usato come chiave della mappa.
     * @param listener Implementazione dell'interfaccia del listener.
     */
    public void subscribe(EventType eventType, EventListener listener) {

        listeners.put(eventType, listener);
    }

    /**
     * Metodo per annullare l'iscrizione di un listener al publisher.
     * @param eventType Tipo dell'evento usato come chiave della mappa.
     */
    public void unsubscribe(EventType eventType) {

        listeners.remove(eventType);
    }

    /**
     * Metodo per notificare i listeners di un dato cambiamento.
     * @param eventType Tipo dell'evento usato come chiave della mappa.
     * @param organizzatore Organizzatore dell'evento.
     * @param turisti Lista di turisti da notificare.
     * @param evento Evento organizzato.
     */
    public void notify(EventType eventType, User organizzatore, List<User> turisti, Evento evento) {

        listeners.get(eventType).update(organizzatore, turisti, evento);
    }
}
