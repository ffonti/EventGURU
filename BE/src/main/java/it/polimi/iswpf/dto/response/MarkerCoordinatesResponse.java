package it.polimi.iswpf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarkerCoordinatesResponse {

    private Long eventoId;

    private String titoloEvento;

    private String nomeLuogo;

    private Float lat;

    private Float lng;
}
