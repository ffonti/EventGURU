package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminCreaModificaEventoRequest {

    private String titolo;

    private String descrizione;

    private LocalDateTime dataInizio;

    private LocalDateTime dataFine;

    private Float lat;

    private Float lng;

    private String nomeLuogo;

    private String usernameOrganizzatore;
}
