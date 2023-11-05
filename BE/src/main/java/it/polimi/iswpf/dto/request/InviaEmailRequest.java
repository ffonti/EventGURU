package it.polimi.iswpf.dto.request;

import it.polimi.iswpf.model._enum.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class InviaEmailRequest {

    private String emailDestinatario;

    private String oggetto;

    private Map<String, String> dynamicData;

    private EventType eventType;
}
