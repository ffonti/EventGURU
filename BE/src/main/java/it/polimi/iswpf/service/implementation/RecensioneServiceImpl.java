package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.RecensioneBuilder;
import it.polimi.iswpf.dto.request.InviaRecensioneRequest;
import it.polimi.iswpf.dto.response.RecensioneResponse;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.ForbiddenException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Recensione;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.repository.RecensioneRepository;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.RecensioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestire tutti i metodi inerenti alle recensioni.
 */
@Service
@RequiredArgsConstructor
public class RecensioneServiceImpl implements RecensioneService {

    private final RecensioneRepository recensioneRepository;
    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;

    /**
     * Metodo per salvare sul database una recensione di un utente.
     * @param request DTO con i dati della recensione -> {@link InviaRecensioneRequest}.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     */
    @Override
    public void inviaRecensione(InviaRecensioneRequest request, Long eventoId, Long turistaId) {

        //L'id autoincrement parte da 1.
        if(eventoId < 1 || turistaId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo l'evento dal db con quell'id.
        Optional<Evento> eventoExists = eventoRepository.findById(eventoId);

        //Se non esiste un evento con quell'id, lancio un'eccezione.
        if(eventoExists.isEmpty()) {
            throw new NotFoundException("Evento non trovato");
        }

        //Prendo il turista dal db con quell'id.
        Optional<User> turistaExists = userRepository.findByUserId(turistaId);

        //Se non esiste un evento con quell'id, lancio un'eccezione.
        if(turistaExists.isEmpty()) {
            throw new NotFoundException("Turista non trovato");
        }

        //Controllo se il ruolo è corretto.
        if(!turistaExists.get().getRuolo().equals(Ruolo.TURISTA)) {
            throw new ForbiddenException("L'utente non ha i permessi adatti");
        }

        //Controllo se il turista è effettivamente iscritto all'evento.
        if(!eventoExists.get().getIscritti().contains(turistaExists.get())) {
            throw new ForbiddenException("Il turista non è iscritto all'evento");
        }

        //Controllo se l'evento è già stato recensito dal turista stesso.
        for(Recensione recensione : eventoExists.get().getRecensioni()) {
            if(recensione.getUser().equals(turistaExists.get())) {
                throw new BadRequestException("Lasciare massimo una recensione per ogni evento");
            }
        }

        //Controllo se il voto è un numero compreso tra 1 e 5.
        if(request.getVoto() < 1 || request.getVoto() > 5) {
            throw new BadRequestException("Inserire un voto valido");
        }

        //Tramite il pattern builder, costruisco l'oggetto recensione da salvare sul database.
        final Recensione recensione = new RecensioneBuilder()
            .voto(request.getVoto())
            .testo(request.getTesto())
            .evento(eventoExists.get())
            .user(turistaExists.get())
            .build();

        //Salvo sul database la recensione
        recensioneRepository.save(recensione);
    }

    /**
     * Metodo per prendere tutte le recensioni di un dato evento.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati delle recensioni -> {@link RecensioneResponse}.
     */
    @Override
    public List<RecensioneResponse> getByEvento(Long eventoId) {

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

        List<Recensione> recensioni = eventoExists.get().getRecensioni();

        //Inizializzo il DTO di risposta.
        List<RecensioneResponse> response = new ArrayList<>();

        //Aggiungo tutte le recensioni al DTO.
        for(Recensione recensione : recensioni) {
            response.add(new RecensioneResponse(
                recensione.getUser().getUsername(),
                recensione.getVoto(),
                recensione.getTesto()
            ));
        }

        return response;
    }
}
