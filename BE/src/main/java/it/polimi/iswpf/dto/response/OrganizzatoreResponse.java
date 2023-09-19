package it.polimi.iswpf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrganizzatoreResponse {

    private Long organizzatoreId;

    private String nome;

    private String cognome;

    private LocalDateTime dataCreazioneAccount;

    private Integer numeroEventiOrganizzati;
}
