package it.polimi.iswpf.dto.response;

import it.polimi.iswpf.model.Stato;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AllEventiResponse {

    private Long eventoId;

    private String titolo;

    private String descrizione;

    private LocalDateTime dataInizio;

    private LocalDateTime dataFine;

    private LocalDateTime dataCreazione;

    private Stato stato;

    private String lat;

    private String lng;

    private String nomeLuogo;

    private String usernameOrganizzatore;

    private List<String> usernameTuristi;
}
