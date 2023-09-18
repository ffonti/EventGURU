package it.polimi.iswpf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecensioneDettagliataResponse {

    private String nomeTurista;

    private String cognomeTurista;

    private String usernameTurista;

    private String testo;

    private Integer voto;

    private LocalDateTime dataCreazioneRecensione;

    private LocalDateTime dataCreazioneTurista;
}
