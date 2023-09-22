package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.response.MarkerCoordinatesResponse;

import java.util.List;

public interface LuogoService {

    List<MarkerCoordinatesResponse> getAllMarkerCoordinates();
}
