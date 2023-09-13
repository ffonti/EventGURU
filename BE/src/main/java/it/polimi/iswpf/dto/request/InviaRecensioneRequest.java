package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviaRecensioneRequest {

    private Integer voto;

    private String testo;
}
