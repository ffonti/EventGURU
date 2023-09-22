package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.response.MarkerCoordinatesResponse;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Luogo;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.service._interface.LuogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LuogoServiceImpl implements LuogoService {

    private final EventoRepository eventoRepository;

    @Override
    public List<MarkerCoordinatesResponse> getAllMarkerCoordinates() {

        List<Evento> eventi = eventoRepository.findAll();
        List<MarkerCoordinatesResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            response.add(new MarkerCoordinatesResponse(
                    evento.getTitolo(),
                    evento.getLuogo().getNome(),
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng()
            ));
        }

        return response;
    }
}
