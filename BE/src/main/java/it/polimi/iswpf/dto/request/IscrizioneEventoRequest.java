package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IscrizioneEventoRequest {

    private Long eventoId;

    private Long turistaId;
}
