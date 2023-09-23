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

import java.time.LocalDateTime;
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
            if(evento.getDataInizio().isAfter(LocalDateTime.now())) {
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

    @Override
    public List<MarkerCoordinatesResponse> coordinateDentroPoligono(List<PuntoPoligono> request) {

        List<Evento> eventi = eventoRepository.findAll();
        List<MarkerCoordinatesResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(isMarkerInsidePolygon(
                Float.parseFloat(evento.getLuogo().getLat()),
                Float.parseFloat(evento.getLuogo().getLng()),
                request) &&
                evento.getDataInizio().isAfter(LocalDateTime.now())) {
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

    @Override
    public List<MarkerCoordinatesResponse> coordinateDentroCirconferenza(DatiCirconferenza request) {

        List<Evento> eventi = eventoRepository.findAll();
        List<MarkerCoordinatesResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(isMarkerInsideCircumference(
                Float.parseFloat(evento.getLuogo().getLat()),
                Float.parseFloat(evento.getLuogo().getLng()),
                request) &&
                evento.getDataInizio().isAfter(LocalDateTime.now())) {
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

    private boolean isMarkerInsideCircumference(float lat, float lng, DatiCirconferenza request) {

        float distanzaDalCentroAlMarker = distanceInKmBetweenEarthCoordinates(
                lat, lng, Float.parseFloat(request.getCentroLat()), Float.parseFloat(request.getCentroLng()));

        return distanzaDalCentroAlMarker <= Float.parseFloat(request.getRaggio());
    }

    private float distanceInKmBetweenEarthCoordinates(float markerLat, float markerLng, float centroLat, float centroLng) {
        final float RAGGIO_TERRA_METRI = 6371000;

        var dLat = degreesToRadians(centroLat - markerLat);
        var dLon = degreesToRadians(centroLng - markerLng);

        markerLat = degreesToRadians(markerLat);
        centroLat = degreesToRadians(centroLat);

        var a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.sin(dLon / 2) *
                                Math.sin(dLon / 2) *
                                Math.cos(markerLat) *
                                Math.cos(centroLat);

        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) (RAGGIO_TERRA_METRI * c);
    }

    private float degreesToRadians(float gradi) {
        return (float) ((gradi * Math.PI) / 180);
    }
}
