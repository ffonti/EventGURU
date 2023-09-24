package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.AllEventiResponse;

import java.util.List;

public interface LuogoService {

    List<AllEventiResponse> getAllMarkerCoordinates();

    List<AllEventiResponse> getAllMarkerCoordinatesByOrganizzatore(Long organizzatoreId);

    List<AllEventiResponse> coordinateDentroPoligono(List<PuntoPoligono> request);

    List<AllEventiResponse> coordinateDentroPoligonoByOrganizzatore(List<PuntoPoligono> request, Long organizzatoreId);

    List<AllEventiResponse> coordinateDentroCirconferenza(DatiCirconferenza request);

    List<AllEventiResponse> coordinateDentroCirconferenzaByOrganizzatore(DatiCirconferenza request, Long organizzatoreId);
}
