package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.EventoResponse;
import it.polimi.iswpf.dto.response.RecensioneResponse;
import it.polimi.iswpf.model.*;
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
    public List<EventoResponse> getAllMarkerCoordinates() {

        List<Evento> eventi = eventoRepository.findAll();
        List<EventoResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(evento.getDataInizio().isAfter(LocalDateTime.now())) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    @Override
    public List<EventoResponse> getAllMarkerCoordinatesByOrganizzatore(Long organizzatoreId) {

        List<Evento> eventi = eventoRepository.findAllByOrganizzatoreUserId(organizzatoreId);
        List<EventoResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            buildCoordinatesResponse(response, evento);
        }

        return response;
    }

    @Override
    public List<EventoResponse> coordinateDentroPoligono(List<PuntoPoligono> request) {

        List<Evento> eventi = eventoRepository.findAll();
        List<EventoResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(isMarkerInsidePolygon(
                Float.parseFloat(evento.getLuogo().getLat()),
                Float.parseFloat(evento.getLuogo().getLng()),
                request) &&
                evento.getDataInizio().isAfter(LocalDateTime.now())) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    @Override
    public List<EventoResponse> coordinateDentroPoligonoByOrganizzatore(List<PuntoPoligono> request, Long organizzatoreId) {

        List<Evento> eventi = eventoRepository.findAllByOrganizzatoreUserId(organizzatoreId);
        List<EventoResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(isMarkerInsidePolygon(
                    Float.parseFloat(evento.getLuogo().getLat()),
                    Float.parseFloat(evento.getLuogo().getLng()),
                    request)) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    @Override
    public List<EventoResponse> coordinateDentroCirconferenza(DatiCirconferenza request) {

        List<Evento> eventi = eventoRepository.findAll();
        List<EventoResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(isMarkerInsideCircumference(
                Float.parseFloat(evento.getLuogo().getLat()),
                Float.parseFloat(evento.getLuogo().getLng()),
                request) &&
                evento.getDataInizio().isAfter(LocalDateTime.now())) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    @Override
    public List<EventoResponse> coordinateDentroCirconferenzaByOrganizzatore(DatiCirconferenza request, Long organizzatoreId) {

        List<Evento> eventi = eventoRepository.findAllByOrganizzatoreUserId(organizzatoreId);
        List<EventoResponse> response = new ArrayList<>();

        for(Evento evento : eventi) {
            if(isMarkerInsideCircumference(
                    Float.parseFloat(evento.getLuogo().getLat()),
                    Float.parseFloat(evento.getLuogo().getLng()),
                    request)) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    private void buildCoordinatesResponse(List<EventoResponse> response, Evento evento) {
        List<String> usernameTuristi;
        List<RecensioneResponse> recensioni;
        usernameTuristi = new ArrayList<>();
        recensioni = new ArrayList<>();

        for(User turista : evento.getIscritti()) {
            usernameTuristi.add(turista.getUsername());
        }

        for(Recensione recensione : evento.getRecensioni()) {
            recensioni.add(new RecensioneResponse(
                    recensione.getUser().getUsername(),
                    recensione.getVoto(),
                    recensione.getTesto()
            ));
        }

        response.add(new EventoResponse(
                evento.getEventoId(),
                evento.getTitolo(),
                evento.getDescrizione(),
                evento.getDataInizio(),
                evento.getDataFine(),
                evento.getDataCreazione(),
                getStatoEvento(evento.getDataInizio(), evento.getDataFine()),
                evento.getLuogo().getLat(),
                evento.getLuogo().getLng(),
                evento.getLuogo().getNome(),
                evento.getOrganizzatore().getUsername(),
                usernameTuristi,
                recensioni
        ));
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

    private Stato getStatoEvento(LocalDateTime dataInizio, LocalDateTime dataFine) {
        if(dataInizio.isAfter(LocalDateTime.now())) {
            return Stato.FUTURO;
        } else if(dataFine.isBefore(LocalDateTime.now())) {
            return Stato.PASSATO;
        } else {
            return Stato.PRESENTE;
        }
    }
}
