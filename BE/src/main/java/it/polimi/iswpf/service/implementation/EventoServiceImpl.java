package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.service._interface.EventoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service per gestire tutti i metodi inerenti agli eventi.
 */
@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;

    @Override
    public void creaEvento(@NonNull CreaEventoRequest request) {

        //Controllo validità di tutti i campi
        if(request.getTitolo().isEmpty() || request.getTitolo().isBlank() ||
            request.getDescrizione().isEmpty() || request.getDescrizione().isBlank() ||
            request.getDataInizio() == null || request.getDataFine() == null) {
            throw new BadRequestException("Compilare tutti i campi");
        }

        Evento evento = new EventoBuilder()
                .titolo(request.getTitolo())
                .descrizione(request.getDescrizione())
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataFine())
                .dataCreazione(LocalDateTime.now())
                .build();

        eventoRepository.save(evento);
    }
}
