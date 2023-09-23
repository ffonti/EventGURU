package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
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
                    evento.getEventoId(),
                    evento.getTitolo(),
                    evento.getLuogo().getNome(),
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng()
            ));
        }

        return response;
    }

    @Override
    public List<MarkerCoordinatesResponse> coordinateDentroPoligono(List<PuntoPoligono> request) {

        List<Evento> eventi = eventoRepository.findAll();
        List<MarkerCoordinatesResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(isMarkerInsidePolygon(
                Float.parseFloat(evento.getLuogo().getLat()),
                Float.parseFloat(evento.getLuogo().getLng()),
                request)) {
                    response.add(new MarkerCoordinatesResponse(
                            evento.getEventoId(),
                            evento.getTitolo(),
                            evento.getLuogo().getNome(),
                            evento.getLuogo().getLat(),
                            evento.getLuogo().getLng()
                    ));
            }
        }

        return response;
    }

    private boolean isMarkerInsidePolygon(float lat, float lng, List<PuntoPoligono> request) {

        boolean isInside = false;

        for(int i = 0, j = request.size() - 1; i < request.size(); j = i++) {

            float latI = Float.parseFloat(request.get(i).getLat());
            float lngI = Float.parseFloat(request.get(i).getLng());

            float latJ = Float.parseFloat(request.get(j).getLat());
            float lngJ = Float.parseFloat(request.get(j).getLng());

            boolean intersect = lngI > lng != lngJ > lng && lat < ((latJ - latI) * (lng - lngI)) / (lngJ - lngI) + latI;

            if(intersect) {
                isInside = !isInside;
            }
        }

        return isInside;
    }

    @Override
    public List<MarkerCoordinatesResponse> coordinateDentroCirconferenza(List<DatiCirconferenza> request) {
        return null;
    }
}
