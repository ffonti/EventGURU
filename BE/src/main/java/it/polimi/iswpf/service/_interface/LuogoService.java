package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.AllEventiResponse;

import java.util.List;

public interface LuogoService {

    List<AllEventiResponse> getAllMarkerCoordinates();

    List<AllEventiResponse> coordinateDentroPoligono(List<PuntoPoligono> request);

    List<AllEventiResponse> coordinateDentroCirconferenza(DatiCirconferenza request);
}
