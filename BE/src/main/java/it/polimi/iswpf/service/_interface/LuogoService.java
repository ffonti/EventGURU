package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.EventoResponse;

import java.util.List;

public interface LuogoService {

    List<EventoResponse> getAllMarkerCoordinates();

    List<EventoResponse> getAllMarkerCoordinatesByOrganizzatore(Long organizzatoreId);

    List<EventoResponse> coordinateDentroPoligono(List<PuntoPoligono> request);

    List<EventoResponse> coordinateDentroPoligonoByOrganizzatore(List<PuntoPoligono> request, Long organizzatoreId);

    List<EventoResponse> coordinateDentroCirconferenza(DatiCirconferenza request);

    List<EventoResponse> coordinateDentroCirconferenzaByOrganizzatore(DatiCirconferenza request, Long organizzatoreId);
}
