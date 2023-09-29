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
 * per inviare una mail a tutti gli iscritti alla newsletter alla creazione di un evento.
 */
@Component
@RequiredArgsConstructor
public class NewsletterListener implements EventListener {

    private final EmailService emailService;

    /**
     * Metodo che invia una mail a tutti gli iscritti alla newsletter per notificare della creazione di un evento.
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
                    "Newsletter EventGURU!",
                    "Ciao, " + turista.getUsername() + "!\n\n" +
                    "L'organizzatore " + organizzatore.getNome() + " " + organizzatore.getCognome() +
                    " ha creato un nuovo evento, di seguito tutte le informazioni necessarie.\n\n" +
                    "Titolo: " + evento.getTitolo() + "\nDescrizione: " + evento.getDescrizione() +
                    "\nData e ora d'inizio: " + evento.getDataInizio() +
                    "\nData e ora di fine: " + evento.getDataFine() +
                    "\n\nSe non vuoi più ricevere email della newsletter, puoi tranquillamente disattivare l'opzione " +
                    "nella sezione \"Account\" sulla piattaforma." +
                    "\n\nCorri ad iscriverti sulla nostra piattaforma!\n\nBuon divertimento,\nIl team EventGURU"
            ));
        }
    }
}
