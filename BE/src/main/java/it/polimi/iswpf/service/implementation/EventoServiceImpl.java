package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.LuogoBuilder;
import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.IscrizioneEventoRequest;
import it.polimi.iswpf.dto.response.EventoResponse;
import it.polimi.iswpf.dto.response.RecensioneResponse;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.ForbiddenException;
import it.polimi.iswpf.exception.InternalServerErrorException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model._enum.EventType;
import it.polimi.iswpf.model._enum.Ruolo;
import it.polimi.iswpf.model._enum.Stato;
import it.polimi.iswpf.model.entity.*;
import it.polimi.iswpf.model.repository.EventoRepository;
import it.polimi.iswpf.model.repository.LuogoRepository;
import it.polimi.iswpf.model.repository.UserRepository;
import it.polimi.iswpf.observer.publisher.EventManager;
import it.polimi.iswpf.service._interface.EventoService;
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
     * @param request DTO con i dati dell'evento da creare.
     */
    @Override
    public void creaEvento(@NonNull CreaModificaEventoRequest request, Long organizzatoreId) {

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

        //Verifico se l'utente è effettivamente un organizzatore.
        if(!organizzatoreExists.get().getRuolo().equals(Ruolo.ORGANIZZATORE)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

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
            request.getLat().isNaN() || request.getLng().isNaN() ||
            request.getLat().isInfinite() || request.getLng().isInfinite() ||
            request.getNomeLuogo().isEmpty() || request.getNomeLuogo().isBlank()) {
            throw new BadRequestException("Compilare tutti i campi");
        }

        //Chiamo il metodo per configurare il luogo dell'evento.
        Luogo luogo = getLuogo(request.getNomeLuogo(), request.getLat(), request.getLng());

        //Costruisco l'oggetto evento da salvare sul database.
        Evento evento = new EventoBuilder()
                .titolo(request.getTitolo())
                .descrizione(request.getDescrizione())
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataFine())
                .dataCreazione(LocalDateTime.now())
                .organizzatore(organizzatoreExists.get())
                .luogo(luogo)
                .build();

        //Salvo l'oggetto evento sul database.
        eventoRepository.save(evento);

        //Prendo dal database i followers di un dato organizzatore.
        List<User> followers = userRepository.findAllBySeguitiAndRuolo(organizzatoreExists.get(), Ruolo.TURISTA);

        //Prendo dal database tutti i turisti iscritti alla newsletter.
        List<User> iscrittiNewsletter = userRepository.findAllByIscrittoNewsletterIsTrueAndRuolo(Ruolo.TURISTA);

        //Rimuovo dalla lista della newsletter gli utenti che sono in entrambe le liste, dando la priorità alla lista dei followers.
        for(User follower : followers) {
            iscrittiNewsletter.remove(follower);
        }

        //Chiamo il metodo notify del publisher, così da notificare le liste della creazione di un evento tramite i pattern Observer e Singleton.
        EventManager.getInstance().notify(EventType.FOLLOWERS, organizzatoreExists.get(), followers, evento);
        EventManager.getInstance().notify(EventType.NEWSLETTER, organizzatoreExists.get(), iscrittiNewsletter, evento);
    }

    /**
     * Metodo per prendere tutti gli eventi di un organizzatore.
     * @param organizzatoreId Id dell'organizzatore di cui si vogliono prendere gli eventi.
     * @return Lista di DTO con i dati degli eventi.
     */
    @Override
    public List<EventoResponse> getAllEventi(Long organizzatoreId) {

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
        List<EventoResponse> response = new ArrayList<>();

        //Lista di username dei turisti iscritti per ogni evento.
        List<String> usernameTuristi;

        //Lista di DTO con i dati di ogni recensione
        List<RecensioneResponse> recensioni;

        //Per ogni evento presente sul database, salvo tutti i campi nell'array di risposta.
        for(Evento evento : eventi.get()) {

            //Inizializzo la lista per ogni evento.
            usernameTuristi = new ArrayList<>();
            recensioni = new ArrayList<>();

            //Aggiungo tutti gli utenti iscritti all'evento.
            for(User turista : evento.getIscritti()) {
                usernameTuristi.add(turista.getUsername());
            }

            //Aggiungo tutte le recensioni dell'evento.
            for(Recensione recensione : evento.getRecensioni()) {
                recensioni.add(new RecensioneResponse(
                        recensione.getUser().getUsername(),
                        recensione.getVoto(),
                        recensione.getTesto()
                ));
            }

            //Costruisco il DTO.
            response.add(new EventoResponse(
                    evento.getEventoId(),
                    evento.getTitolo(),
                    evento.getDescrizione(),
                    evento.getDataInizio(),
                    evento.getDataFine(),
                    evento.getDataCreazione(),
                    getStatoEvento(evento.getDataInizio(), evento.getDataFine()),
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng(),
                    evento.getLuogo().getNome(),
                    evento.getOrganizzatore().getUsername(),
                    usernameTuristi,
                    recensioni
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

        //Controllo se esiste ancora l'evento con quell'id.
        Optional<Evento> eventoDeleted = eventoRepository.findById(eventoId);

        //Se non è stato eliminato, lancio un'eccezione.
        if(eventoDeleted.isPresent()) {
            throw new InternalServerErrorException("Errore nell'eliminazione dell'evento");
        }
    }

    /**
     * Metodo per prendere un evento dato un id.
     * @param eventoId Id del singolo evento.
     * @return DTO con i dati dell'evento richiesto.
     */
    @Override
    public EventoResponse getEventoById(Long eventoId) {

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

        //Lista di username dei turisti iscritti all'evento.
        List<String> usernameTuristi = new ArrayList<>();

        //Lista di DTO con i dati delle recensioni dell'evento
        List<RecensioneResponse> recensioni = new ArrayList<>();

        //Salvo tutti i campi nell'array di risposta.
        for(User turista : eventoExists.get().getIscritti()) {
            usernameTuristi.add(turista.getUsername());
        }

        //Aggiungo tutte le recensioni dell'evento.
        for(Recensione recensione : eventoExists.get().getRecensioni()) {
            recensioni.add(new RecensioneResponse(
                    recensione.getUser().getUsername(),
                    recensione.getVoto(),
                    recensione.getTesto()
            ));
        }

        //Costruisco il DTO.
        return new EventoResponse(
                eventoExists.get().getEventoId(),
                eventoExists.get().getTitolo(),
                eventoExists.get().getDescrizione(),
                eventoExists.get().getDataInizio(),
                eventoExists.get().getDataFine(),
                eventoExists.get().getDataCreazione(),
                getStatoEvento(eventoExists.get().getDataInizio(), eventoExists.get().getDataFine()),
                eventoExists.get().getLuogo().getLat(),
                eventoExists.get().getLuogo().getLng(),
                eventoExists.get().getLuogo().getNome(),
                eventoExists.get().getOrganizzatore().getUsername(),
                usernameTuristi,
                recensioni
        );
    }

    /**
     * Metodo per modificare i dati di un evento già esistente.
     * @param request DTO con nuovi dati per aggiornare l'evento.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     */
    @Override
    public void modificaEvento(CreaModificaEventoRequest request, Long eventoId) {

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

        //Se l'evento esiste, lo assegno a una variabile.
        Evento evento = eventoExists.get();

        //Se il client ha compilato il campo "Titolo" e non è vuoto, aggiorno il titolo dell'evento.
        if(!request.getTitolo().isEmpty() && !request.getTitolo().isBlank()) {
            evento.setTitolo(request.getTitolo());
        }

        //Se il client ha compilato il campo "Descrizione" e non è vuoto, aggiorno la descrizione dell'evento.
        if(!request.getDescrizione().isEmpty() && !request.getDescrizione().isBlank()) {
            evento.setDescrizione(request.getDescrizione());
        }

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

        //Aggiorno la data di inizio e fine dell'evento.
        evento.setDataInizio(request.getDataInizio());
        evento.setDataFine(request.getDataFine());

        //Se il client ha compilato il campo "Nome luogo" e non è vuoto, aggiorno il nome del luogo dell'evento.
        if(!request.getNomeLuogo().isEmpty() && !request.getNomeLuogo().isBlank()) {

            //Verifico se il luogo con un dato nome è già salvato sul database.
            Optional<Luogo> luogoExists = luogoRepository.getLuogoByNome(request.getNomeLuogo());

            Luogo luogo;

            if(luogoExists.isEmpty()) {
                //Se non esiste nessun luogo con un dato nome, costruisco l'oggetto.
                luogo = new LuogoBuilder()
                        .nome(request.getNomeLuogo())
                        .lat(request.getLat())
                        .lng(request.getLng())
                        .build();

                //Salvo sul database l'oggetto appena costruito.
                luogoRepository.save(luogo);

            } else {
                //Se esiste già un luogo con quel nome, lo utilizzo per salvare l'evento.
                luogo = luogoExists.get();
            }

            //Aggiorno il luogo dell'evento.
            evento.setLuogo(luogo);
        }

        //Chiamo la repository e salvo i dati aggiornati dell'evento.
        eventoRepository.save(evento);
    }

    /**
     * Metodo per prendere tutti gli eventi presenti sul database.
     * @return Lista di DTO con tutti i dati di ogni evento.
     */
    @Override
    public List<EventoResponse> adminGetAllEventi() {

        //Prendo tutti gli eventi presenti sul database.
        List<Evento> eventi = eventoRepository.findAll();

        //Se non è presente nessun evento lancio un'eccezione.
        if(eventi.isEmpty()) {
            throw new NotFoundException("Eventi non trovati");
        }

        //Se non sono presenti eventi, ritorno un array vuoto.
        List<EventoResponse> response = new ArrayList<>();

        //Lista di username dei turisti iscritti per ogni evento.
        List<String> usernameTuristi;

        //Lista di recensioni per ogni evento.
        List<RecensioneResponse> recensioni;

        //Per ogni evento, aggiungo all'array di risposta i dati dell'evento stesso.
        for(Evento evento : eventi) {

            //Inizializzo le liste per ogni evento.
            usernameTuristi = new ArrayList<>();
            recensioni = new ArrayList<>();

            //Aggiungo tutti gli utenti iscritti all'evento.
            for(User turista : evento.getIscritti()) {
                usernameTuristi.add(turista.getUsername());
            }

            //Aggiungo tutte le recensioni dell'evento.
            for(Recensione recensione : evento.getRecensioni()) {
                recensioni.add(new RecensioneResponse(
                        recensione.getUser().getUsername(),
                        recensione.getVoto(),
                        recensione.getTesto()
                ));
            }

            //Costruisco il DTO.
            response.add(new EventoResponse(
                    evento.getEventoId(),
                    evento.getTitolo(),
                    evento.getDescrizione(),
                    evento.getDataInizio(),
                    evento.getDataFine(),
                    evento.getDataCreazione(),
                    getStatoEvento(evento.getDataInizio(), evento.getDataFine()),
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng(),
                    evento.getLuogo().getNome(),
                    evento.getOrganizzatore().getUsername(),
                    usernameTuristi,
                    recensioni
            ));
        }

        return response;
    }

    /**
     * Metodo per gli admin. Permette di creare un evento scegliendo l'organizzatore.
     * Salva l'utente sul db dopo aver effettuato tutti i controlli di validità dei dati.
     * @param request DTO con i dati dell'evento da creare.
     */
    @Override
    public void adminCreaEvento(AdminCreaModificaEventoRequest request) {

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
                request.getLat().isNaN() || request.getLng().isNaN() ||
                request.getLat().isInfinite() || request.getLng().isInfinite() ||
                request.getNomeLuogo().isEmpty() || request.getNomeLuogo().isBlank() ||
                request.getUsernameOrganizzatore().isEmpty() || request.getUsernameOrganizzatore().isBlank()) {
            throw new BadRequestException("Compilare tutti i campi");
        }

        //Chiamo il metodo per configurare il luogo dell'evento.
        Luogo luogo = getLuogo(request.getNomeLuogo(), request.getLat(), request.getLng());

        //Verifico se esiste un organizzatore con quell'username sul database.
        Optional<User> organizzatoreExists = userRepository.findByUsername(request.getUsernameOrganizzatore());

        //Se non esiste, lancio un'eccezione.
        if(organizzatoreExists.isEmpty()) {
            throw new BadRequestException("Non esiste alcun organizzatore con questo username");
        }

        //Se non è un organizzatore, lancio un'eccezione.
        if(!organizzatoreExists.get().getRuolo().equals(Ruolo.ORGANIZZATORE)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Costruisco l'oggetto evento da salvare sul database.
        Evento evento = new EventoBuilder()
                .titolo(request.getTitolo())
                .descrizione(request.getDescrizione())
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataFine())
                .dataCreazione(LocalDateTime.now())
                .organizzatore(organizzatoreExists.get())
                .luogo(luogo)
                .build();

        //Salvo l'oggetto evento sul database.
        eventoRepository.save(evento);
    }

    /**
     * Metodo per gli admin, usato per modificare i dati di un evento già esistente.
     * @param request DTO con nuovi dati per aggiornare l'evento.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     */
    @Override
    public void adminModificaEvento(AdminCreaModificaEventoRequest request, Long eventoId) {

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

        //Se l'evento esiste, lo assegno a una variabile.
        Evento evento = eventoExists.get();

        //Se il client ha compilato il campo "Titolo" e non è vuoto, aggiorno il titolo dell'evento.
        if(!request.getTitolo().isEmpty() && !request.getTitolo().isBlank()) {
            evento.setTitolo(request.getTitolo());
        }

        //Se il client ha compilato il campo "Descrizione" e non è vuoto, aggiorno la descrizione dell'evento.
        if(!request.getDescrizione().isEmpty() && !request.getDescrizione().isBlank()) {
            evento.setDescrizione(request.getDescrizione());
        }

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

        //Aggiorno la data di inizio e fine dell'evento.
        evento.setDataInizio(request.getDataInizio());
        evento.setDataFine(request.getDataFine());

        //Se il client ha compilato il campo "Nome luogo" e non è vuoto, aggiorno il nome del luogo dell'evento.
        if(!request.getNomeLuogo().isEmpty() && !request.getNomeLuogo().isBlank()) {

            //Verifico se il luogo con un dato nome è già salvato sul database.
            Optional<Luogo> luogoExists = luogoRepository.getLuogoByNome(request.getNomeLuogo());

            Luogo luogo;

            if(luogoExists.isEmpty()) {
                //Se non esiste nessun luogo con un dato nome, costruisco l'oggetto.
                luogo = new LuogoBuilder()
                        .nome(request.getNomeLuogo())
                        .lat(request.getLat())
                        .lng(request.getLng())
                        .build();

                //Salvo sul database l'oggetto appena costruito.
                luogoRepository.save(luogo);

            } else {
                //Se esiste già un luogo con quel nome, lo utilizzo per salvare l'evento.
                luogo = luogoExists.get();
            }

            //Aggiorno il luogo dell'evento.
            evento.setLuogo(luogo);
        }

        //Se il client ha compilato il campo "Organizzatore" e non è vuoto, aggiorno l'username dell'organizzatore dell'evento.
        if(!request.getUsernameOrganizzatore().isBlank() && !request.getUsernameOrganizzatore().isEmpty()) {

            //Prendo dal db l'organizzatore con quell'username.
            Optional<User> organizzatoreExists = userRepository.findByUsername(request.getUsernameOrganizzatore());

            //Se non esiste, lancio un'eccezione.
            if(organizzatoreExists.isEmpty()) {
                throw new BadRequestException("Non esiste alcun organizzatore con questo username");
            }

            //Se non è un organizzatore, lancio un'eccezione.
            if(!organizzatoreExists.get().getRuolo().equals(Ruolo.ORGANIZZATORE)) {
                throw new ForbiddenException("L'utente non ha i permessi adatti");
            }

            //Cambio l'organizzatore dell'evento.
            evento.setOrganizzatore(organizzatoreExists.get());
        }

        //Chiamo la repository e salvo i dati aggiornati dell'evento.
        eventoRepository.save(evento);
    }

    /**
     * Metodo per iscrivere un turista a un evento.
     * @param request DTO con gli id univoci dell'evento e del turista.
     */
    @Override
    public void iscrizioneEvento(IscrizioneEventoRequest request) {

        //Gli id autoincrement partono da 1.
        if(request.getEventoId() < 1 || request.getTuristaId() < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'evento dal db con quell'id.
        Optional<Evento> eventoExists = eventoRepository.findById(request.getEventoId());

        //Se non esiste un evento con quell'id, lancio un'eccezione.
        if(eventoExists.isEmpty()) {
            throw new NotFoundException("Evento non trovato");
        }

        //Prendo il turista dal db con quell'id.
        Optional<User> turistaExists = userRepository.findByUserId(request.getTuristaId());

        //Se non esiste un turista con quell'id, lancio un'eccezione.
        if(turistaExists.isEmpty()) {
            throw new NotFoundException("Turista non trovato");
        }

        //Controllo se il ruolo è corretto.
        if(!turistaExists.get().getRuolo().equals(Ruolo.TURISTA)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Salvo in delle variabili i dati presi dal database, per poterli aggiornare.
        Evento evento = eventoExists.get();
        User turista = turistaExists.get();

        //Se l'evento non ha ancora nessun iscritto, inizializzo l'array e aggiungo il primo turista.
        if(evento.getIscritti().isEmpty()) {
            evento.setIscritti(new ArrayList<>(List.of(turista)));

        } else {
            //Se l'evento ha già degli iscritti, controllo che il turista non sia già iscritto all'evento.
            for(User turistaIscritto: evento.getIscritti()) {
                if(turistaIscritto.getUserId().equals(turista.getUserId())) {
                    throw new BadRequestException("Il turista è già iscritto all'evento");
                }
            }

            //Passati i controlli aggiorno la lista di utenti.
            evento.getIscritti().add(turista);
        }

        //Aggiorno l'evento sul database.
        eventoRepository.save(evento);
    }

    /**
     * Metodo per annullare l'iscrizione di un turista a un evento.
     * @param request DTO con gli id univoci dell'evento e del turista.
     */
    @Override
    public void annullaIscrizione(IscrizioneEventoRequest request) {

        //Gli id autoincrement partono da 1.
        if(request.getEventoId() < 1 || request.getTuristaId() < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'evento dal db con quell'id.
        Optional<Evento> eventoExists = eventoRepository.findById(request.getEventoId());

        //Se non esiste un evento con quell'id, lancio un'eccezione.
        if(eventoExists.isEmpty()) {
            throw new NotFoundException("Evento non trovato");
        }

        //Prendo il turista dal db con quell'id.
        Optional<User> turistaExists = userRepository.findByUserId(request.getTuristaId());

        //Se non esiste un turista con quell'id, lancio un'eccezione.
        if(turistaExists.isEmpty()) {
            throw new NotFoundException("Turista non trovato");
        }

        //Controllo se il ruolo è corretto.
        if(!turistaExists.get().getRuolo().equals(Ruolo.TURISTA)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Salvo in delle variabili i dati presi dal database, per poterli aggiornare.
        Evento evento = eventoExists.get();
        User turista = turistaExists.get();

        //Rimuovo il turista dalla lista degli iscritti.
        evento.getIscritti().remove(turista);

        //Salvo le modifiche sul database.
        eventoRepository.save(evento);
    }

    /**
     * Metodo per prendere tutti gli eventi a cui è iscritto un turista.
     * @param usernameTurista Username del turista, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con tutti i dati di ogni evento.
     */
    @Override
    public List<EventoResponse> getEventiByTurista(String usernameTurista) {

        //Controllo la validità del campo "Username".
        if(usernameTurista.isEmpty() || usernameTurista.isBlank()) {
            throw new BadRequestException("Inserire un username valido");
        }

        //Prendo dal db il turista con quell'username.
        Optional<User> turistaExists = userRepository.findByUsername(usernameTurista);

        //Se non esiste o il ruolo non è corretto, lancio un'eccezione.
        if(turistaExists.isEmpty() || !turistaExists.get().getRuolo().equals(Ruolo.TURISTA)) {
            throw new NotFoundException("Non esiste un turista con questo username");
        }

        //Prendo dal database tutti gli eventi a cui il turista è iscritto.
        List<Evento> eventi = eventoRepository.findAllByIscrittiIsContaining(turistaExists.get());

        //Se non sono presenti eventi, ritorno un array vuoto.
        List<EventoResponse> response = new ArrayList<>();

        //Lista di username dei turisti iscritti per ogni evento.
        List<String> usernameTuristi;

        //Lista di recensioni per ogni evento.
        List<RecensioneResponse> recensioni;

        //Per ogni evento, aggiungo all'array di risposta i dati dell'evento stesso.
        for(Evento evento : eventi) {

            //Inizializzo le liste per ogni evento.
            usernameTuristi = new ArrayList<>();
            recensioni = new ArrayList<>();

            //Aggiungo tutti gli utenti iscritti all'evento.
            for(User turista : evento.getIscritti()) {
                usernameTuristi.add(turista.getUsername());
            }

            //Aggiungo tutte le recensioni dell'evento.
            for(Recensione recensione : evento.getRecensioni()) {
                recensioni.add(new RecensioneResponse(
                        recensione.getUser().getUsername(),
                        recensione.getVoto(),
                        recensione.getTesto()
                ));
            }

            //Costruisco il DTO.
            response.add(new EventoResponse(
                    evento.getEventoId(),
                    evento.getTitolo(),
                    evento.getDescrizione(),
                    evento.getDataInizio(),
                    evento.getDataFine(),
                    evento.getDataCreazione(),
                    getStatoEvento(evento.getDataInizio(), evento.getDataFine()),
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng(),
                    evento.getLuogo().getNome(),
                    evento.getOrganizzatore().getUsername(),
                    usernameTuristi,
                    recensioni
            ));
        }

        return response;
    }

    /**
     * Metodo che permette a un organizzatore di rimuovere un turista iscritto a un dato evento.
     * @param usernameTurista Username univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     */
    @Override
    public void rimuoviTuristaDaEvento(String usernameTurista, Long eventoId) {

        //Controllo la validità del campo "Username".
        if(usernameTurista.isEmpty() || usernameTurista.isBlank()) {
            throw new BadRequestException("Inserire un username valido");
        }

        //Chiamo il database per controllare se esiste un turista con questo username.
        Optional<User> turistaExists = userRepository.findByUsername(usernameTurista);

        //Se non esiste o il ruolo non è corretto, lancio un'eccezione.
        if(turistaExists.isEmpty() || !turistaExists.get().getRuolo().equals(Ruolo.TURISTA)) {
            throw new NotFoundException("Non esiste un turista con questo username");
        }

        //L'id univoco è sempre maggiore o uguale a 1.
        if(eventoId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'evento dal db con quell'id.
        Optional<Evento> eventoExists = eventoRepository.findById(eventoId);

        //Se non esiste un evento con quell'id, lancio un'eccezione.
        if(eventoExists.isEmpty()) {
            throw new NotFoundException("Evento non trovato");
        }

        Evento evento = eventoExists.get();

        //Controllo se il turista è effettivamente iscritto all'evento.
        if(!evento.getIscritti().contains(turistaExists.get())) {
            throw new BadRequestException("Il turista non risulta iscritto all'evento");
        }

        //Rimuovo il turista dalla lista di iscritti.
        evento.getIscritti().remove(turistaExists.get());

        //Salvo le modifiche sul database.
        eventoRepository.save(evento);
    }

    /**
     * Ricevuti in ingresso data di inizio e data di fine di un evento, restituisce lo stato basato su data e ora attuali.
     * @param dataInizio Data di inizio dell'evento.
     * @param dataFine Data di fine dell'evento.
     * @return Lo stato dell'evento (passato, presente o futuro).
     */
    private Stato getStatoEvento(LocalDateTime dataInizio, LocalDateTime dataFine) {

        return dataInizio.isAfter(LocalDateTime.now()) ? Stato.FUTURO
                : dataFine.isBefore(LocalDateTime.now()) ? Stato.PASSATO
                : Stato.PRESENTE;
    }

    /**
     * Code cleaning. Metodo usato da due endpoint (admin e no) per gestire il luogo di un evento.
     * @param nomeLuogo Nome del luogo dell'evento.
     * @param lat Latitudine del luogo dell'evento.
     * @param lng Longitudine del luogo dell'evento.
     * @return Un'istanza di {@link Luogo}, dopo averla salvata sul database (in caso non fosse presente).
     */
    private Luogo getLuogo(String nomeLuogo, Float lat, Float lng) {

        //Prendo dal db il luogo con quel nome.
        Optional<Luogo> luogoExists = luogoRepository.getLuogoByNome(nomeLuogo);

        Luogo luogo;

        //Se non esiste nessun luogo con un dato nome, costruisco l'oggetto tramite il pattern Builder.
        if(luogoExists.isEmpty()) {
            luogo = new LuogoBuilder()
                    .lat(lat)
                    .lng(lng)
                    .nome(nomeLuogo)
                    .build();

            //Salvo sul database l'oggetto appena costruito
            luogoRepository.save(luogo);

        } else {
            //Se esiste già un luogo con quel nome, lo utilizzo per salvare l'evento.
            luogo = luogoExists.get();
        }

        return luogo;
    }
}
