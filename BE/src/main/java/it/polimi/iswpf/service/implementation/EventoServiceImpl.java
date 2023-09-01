package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.LuogoBuilder;
import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.dto.response.GetEventoResponse;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.ForbiddenException;
import it.polimi.iswpf.exception.InternalServerErrorException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Luogo;
import it.polimi.iswpf.model.Stato;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.repository.LuogoRepository;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.EventoService;
import it.polimi.iswpf.util.SessionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestire tutti i metodi inerenti agli eventi.
 */
@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;
    private final LuogoRepository luogoRepository;

    /**
     * Metodo per creare un evento. Salva l'utente sul db dopo aver effettuato tutti i controlli di validità dei dati.
     * @param request DTO con i dati dell'evento da creare -> {@link CreaEventoRequest}.
     */
    @Override
    public void creaEvento(@NonNull CreaEventoRequest request) {

        //La data di inizio dell'evento deve essere precedente alla data di fine dell'evento.
        if(request.getDataFine().isBefore(request.getDataInizio()) ||
            request.getDataInizio().isEqual(request.getDataFine())) {
            throw new BadRequestException("La data di inizio deve essere precedente alla data di fine");
        }

        //La data di inizio dell'evento deve essere precedente alla data attuale.
        if(request.getDataInizio().isBefore(LocalDateTime.now()) ||
            request.getDataInizio().isEqual(LocalDateTime.now())) {
            throw new BadRequestException("L'evento non può avvenire nel passato");
        }

        //Controllo validità di tutti i campi.
        if(request.getTitolo().isEmpty() || request.getTitolo().isBlank() ||
            request.getDescrizione().isEmpty() || request.getDescrizione().isBlank() ||
            request.getLng().isEmpty() || request.getLng().isBlank() ||
            request.getLat().isEmpty() || request.getLat().isBlank() ||
            request.getNomeLuogo().isEmpty() || request.getNomeLuogo().isBlank()) {
            throw new BadRequestException("Compilare tutti i campi");
        }

        //Prendo i dati dell'utente in sessione.
        User organizzatore = SessionManager.getInstance().getLoggedUser();

        //Verifico se l'utente è effettivamente un organizzatore.
        if(!organizzatore.getRuolo().toString().equals("ORGANIZZATORE")) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Verifico se il luogo con un dato nome è già salvato sul database.
        Optional<Luogo> luogoExists = luogoRepository.getLuogoByNome(request.getNomeLuogo());

        Luogo luogo;

        if(luogoExists.isEmpty()) {
            //Se non esiste nessun luogo con un dato nome, costruisco l'oggetto.
            luogo = new LuogoBuilder()
                    .lat(request.getLat())
                    .lng(request.getLng())
                    .nome(request.getNomeLuogo())
                    .build();

            //Salvo sul database l'oggetto appena costruito
            luogoRepository.save(luogo);

        } else {
            //Se esiste già un luogo con quel nome, lo utilizzo per salvare l'evento.
            luogo = luogoExists.get();
        }

        //Costruisco l'oggetto evento da salvare sul database.
        Evento evento = new EventoBuilder()
                .titolo(request.getTitolo())
                .descrizione(request.getDescrizione())
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataFine())
                .dataCreazione(LocalDateTime.now())
                .organizzatore(organizzatore)
                .luogo(luogo)
                .build();

        //Salvo l'oggetto evento sul database.
        eventoRepository.save(evento);
    }

    /**
     * Metodo per prendere tutti gli eventi di un organizzatore.
     * @param organizzatoreId Id dell'organizzatore di cui si vogliono prendere gli eventi.
     * @return Lista di DTO con i dati degli eventi {@link GetEventoResponse}.
     */
    @Override
    public List<GetEventoResponse> getAllEventi(Long organizzatoreId) {

        //L'id autoincrement parte da 1.
        if(organizzatoreId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'utente dal db con quell'id.
        Optional<User> organizzatoreExists = userRepository.findByUserId(organizzatoreId);

        //Se non esiste un utente con quell'id, lancio un'eccezione.
        if(organizzatoreExists.isEmpty()) {
            throw new NotFoundException("Organizzatore non trovato");
        }

        //Chiedo al database se esistono eventi organizzati da uno specifico utente.
        Optional<List<Evento>> eventi = eventoRepository.findAllByOrganizzatore(organizzatoreExists.get());

        //Se non ci sono eventi, ritorno un array vuoto.
        if(eventi.isEmpty()) {
            return new ArrayList<>();
        }

        //Se sono presenti eventi, inizializzo l'array che conterrà gli eventi.
        List<GetEventoResponse> response = new ArrayList<>();

        //Per ogni evento presente sul database, salvo tutti i campi nell'array di risposta.
        for(Evento evento: eventi.get()) {

            response.add(new GetEventoResponse(
                    evento.getEventoId(),
                    evento.getTitolo(),
                    evento.getDescrizione(),
                    evento.getDataInizio(),
                    evento.getDataFine(),
                    evento.getDataCreazione(),
                    getStatoEvento(evento.getDataInizio(), evento.getDataFine()),
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng(),
                    evento.getLuogo().getNome()
            ));
        }

        return response;
    }

    /**
     * Metodo per eliminare un evento.
     * @param eventoId Id dell'evento da eliminare.
     */
    @Override
    public void eliminaEvento(Long eventoId) {

        //L'id autoincrement parte da 1.
        if(eventoId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'evento dal db con quell'id.
        Optional<Evento> eventoExists = eventoRepository.findById(eventoId);

        //Se non esiste un evento con quell'id, lancio un'eccezione.
        if(eventoExists.isEmpty()) {
            throw new NotFoundException("Evento non trovato");
        }

        //Elimino l'evento dal database.
        eventoRepository.delete(eventoExists.get());

        //Controllo se esiste ancora l'evento con quell'id
        Optional<Evento> eventoDeleted = eventoRepository.findById(eventoId);

        //Se non è stato eliminato, lancio un'eccezione.
        if(eventoDeleted.isPresent()) {
            throw new InternalServerErrorException("Errore nell'eliminazione dell'evento");
        }
    }

    /**
     * Metodo per prendere un evento dato un id.
     * @param eventoId Id del singolo evento.
     * @return DTO con i dati dell'evento richiesto -> {@link GetEventoResponse}.
     */
    @Override
    public GetEventoResponse getEventoById(Long eventoId) {

        //L'id autoincrement parte da 1.
        if(eventoId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'evento dal db con quell'id.
        Optional<Evento> eventoExists = eventoRepository.findById(eventoId);

        //Se non esiste un evento con quell'id, lancio un'eccezione.
        if(eventoExists.isEmpty()) {
            throw new NotFoundException("Evento non trovato");
        }

        //Se esiste, ritorno l'oggetto evento.
        return new GetEventoResponse(
                eventoExists.get().getEventoId(),
                eventoExists.get().getTitolo(),
                eventoExists.get().getDescrizione(),
                eventoExists.get().getDataInizio(),
                eventoExists.get().getDataFine(),
                eventoExists.get().getDataCreazione(),
                getStatoEvento(eventoExists.get().getDataInizio(), eventoExists.get().getDataFine()),
                eventoExists.get().getLuogo().getLat(),
                eventoExists.get().getLuogo().getLng(),
                eventoExists.get().getLuogo().getNome()
        );
    }

    private Stato getStatoEvento(LocalDateTime dataInizio, LocalDateTime dataFine) {
        if(dataInizio.isAfter(LocalDateTime.now())) {
            return Stato.FUTURO;
        } else if(dataFine.isBefore(LocalDateTime.now())) {
            return Stato.PASSATO;
        } else {
            return Stato.PRESENTE;
        }
    }
}
