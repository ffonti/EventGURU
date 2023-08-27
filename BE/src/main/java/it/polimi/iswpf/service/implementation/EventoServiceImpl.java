package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.service._interface.EventoService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service per gestire tutti i metodi inerenti agli eventi.
 */
@Service
public class EventoServiceImpl implements EventoService {

    @Override
    public void creaEvento(@NonNull CreaEventoRequest request) {

    }
}
