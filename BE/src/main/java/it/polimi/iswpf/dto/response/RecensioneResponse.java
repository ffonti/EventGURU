package it.polimi.iswpf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecensioneResponse {

    private String usernameTurista;

    private Integer voto;

    private String testo;
}
