package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminCreaEventoRequest {

    private String titolo;

    private String descrizione;

    private LocalDateTime dataInizio;

    private LocalDateTime dataFine;

    private String lat;

    private String lng;

    private String nomeLuogo;

    private String usernameOrganizzatore;
}
