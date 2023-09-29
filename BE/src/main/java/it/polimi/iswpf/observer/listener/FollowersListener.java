package it.polimi.iswpf.observer.listener;

import it.polimi.iswpf.dto.request.InviaEmailRequest;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.service._interface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementazione dell'interfaccia del listener che contiene la logica nel metodo update
 * per inviare una mail ai followers di un organizzatore alla creazione di un evento.
 */
@Component
@RequiredArgsConstructor
public class FollowersListener implements EventListener {

    private final EmailService emailService;

    /**
     * Metodo che invia una mail ai followers dell'organizzatore per notificare della creazione di un evento.
     * @param organizzatore Organizzatore dell'evento.
     * @param turisti Lista di turisti da notificare.
     * @param evento Evento organizzato.
     */
    @Override
    public void update(User organizzatore, List<User> turisti, Evento evento) {

        //Scorro la lista di turisti per inviare singolarmente le mail.
        for(User turista : turisti) {

            //Chiamo il service adatto per l'invio delle mail passandogli il DTO con tutti i dati.
            emailService.inviaEmail(new InviaEmailRequest(
                    turista.getEmail(),
                    "Nuovo evento in EventGURU!",
                    "Ciao, " + turista.getUsername() + "!\n\n" +
                    "L'organizzatore " + organizzatore.getNome() + " " + organizzatore.getCognome() +
                    " da te seguito, ha creato un nuovo evento, di seguito tutte le informazioni necessarie.\n\n" +
                    "Titolo: " + evento.getTitolo() + "\nDescrizione: " + evento.getDescrizione() +
                    "\nData e ora d'inizio: " + evento.getDataInizio() +
                    "\nData e ora di fine: " + evento.getDataFine() +
                    "\n\nSe non vuoi più ricevere email alla creazione di eventi da parte di " +
                    organizzatore.getNome() + " " + organizzatore.getCognome() + ", puoi tranquillamente smettere " +
                    "di seguirlo dalla sezione \"Organizzatori\" sulla piattaforma." +
                    "\n\nCorri ad iscriverti sulla nostra piattaforma!\n\nBuon divertimento,\nIl team EventGURU"
            ));
        }
    }
}
