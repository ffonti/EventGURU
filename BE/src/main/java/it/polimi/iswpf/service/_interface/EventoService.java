package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.dto.response.GetAllEventiByOrganizzatoreResponse;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.service.implementation.EventoServiceImpl;

import java.util.List;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link EventoServiceImpl}.
 */
public interface EventoService {

    void creaEvento(CreaEventoRequest request);

    List<GetAllEventiByOrganizzatoreResponse> getAllEventi(Long organizzatoreId);
}
