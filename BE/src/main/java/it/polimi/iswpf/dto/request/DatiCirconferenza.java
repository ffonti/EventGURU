package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatiCirconferenza {

    private Float centroLat;

    private Float centroLng;

    private Float raggio;
}
