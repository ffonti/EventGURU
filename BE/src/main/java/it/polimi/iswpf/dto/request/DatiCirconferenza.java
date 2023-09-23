package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatiCirconferenza {

    private String centroLat;

    private String centroLng;

    private String raggio;
}
