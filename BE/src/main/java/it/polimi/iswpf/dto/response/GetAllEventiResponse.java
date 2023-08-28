package it.polimi.iswpf.dto.response;

import it.polimi.iswpf.model.Evento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllEventiResponse {

    private List<Evento> eventi;
}
