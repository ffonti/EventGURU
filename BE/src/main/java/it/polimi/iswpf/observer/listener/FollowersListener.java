package it.polimi.iswpf.observer.listener;

import it.polimi.iswpf.dto.request.InviaEmailRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.model.EventType;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.service._interface.EmailService;
import it.polimi.iswpf.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        if(organizzatore.getNome().isEmpty() || organizzatore.getNome().isBlank() ||
                organizzatore.getCognome().isEmpty() || organizzatore.getCognome().isBlank() ||
                evento.getTitolo().isEmpty() || evento.getTitolo().isBlank() ||
                evento.getDescrizione().isEmpty() || evento.getDescrizione().isBlank() ||
                evento.getDataInizio() == null || evento.getDataFine() == null) {
            throw new BadRequestException("Campi non esistenti");
        }

        //HashMap dove salvare i dati dinamici da inserire nel template per personalizzare le mail.
        Map<String, String> dynamicData = new HashMap<>();

        //Aggiungo i dati e stabilisco una chiave tramite la quale accedo all'interno del template.
        dynamicData.put("nomeOrganizzatore", organizzatore.getNome());
        dynamicData.put("cognomeOrganizzatore", organizzatore.getCognome());
        dynamicData.put("titolo", evento.getTitolo());
        dynamicData.put("descrizione", evento.getDescrizione());
        dynamicData.put("dataInizio", UtilMethods.getInstance().dateToString(evento.getDataInizio()));
        dynamicData.put("dataFine", UtilMethods.getInstance().dateToString(evento.getDataFine()));

        //Scorro la lista di turisti per inviare singolarmente le mail.
        for(User turista : turisti) {

            if(turista.getNome().isBlank() || turista.getNome().isEmpty() ||
                    turista.getEmail().isBlank() || turista.getEmail().isEmpty()) {
                throw new BadRequestException("Campi non esistenti");
            }

            //Aggiungo l'unico dato diverso per ogni mail.
            dynamicData.put("nomeDestinatario", turista.getNome());

            //Chiamo il service adatto per l'invio delle mail passandogli il DTO con tutti i dati.
            emailService.inviaEmail(new InviaEmailRequest(
                    turista.getEmail(),
                    "Newsletter EventGURU",
                    dynamicData,
                    EventType.FOLLOWERS
            ));
        }
    }
}
