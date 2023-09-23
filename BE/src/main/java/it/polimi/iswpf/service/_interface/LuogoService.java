package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.MarkerCoordinatesResponse;

import java.util.List;

public interface LuogoService {

    List<MarkerCoordinatesResponse> getAllMarkerCoordinates();

    List<MarkerCoordinatesResponse> coordinateDentroPoligono(List<PuntoPoligono> request);

    List<MarkerCoordinatesResponse> coordinateDentroCirconferenza(List<DatiCirconferenza> request);
}
