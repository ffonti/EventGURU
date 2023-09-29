package it.polimi.iswpf.observer.listener;

import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.User;

import java.util.List;

/**
 * Interfaccia che rappresenta il listener del pattern Observer. Contiene la firma del
 * metodo update chiamato per aggiornare gli iscritti alla creazione di un evento.
 */
public interface EventListener {

    /**
     * Metodo chiamato per aggiornare gli utenti alla creazione di un evento.
     * @param organizzatore Organizzatore dell'evento.
     * @param turisti Lista di turisti da notificare.
     * @param evento Evento organizzato.
     */
    void update(User organizzatore, List<User> turisti, Evento evento);
}
