package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.EventoResponse;
import it.polimi.iswpf.dto.response.RecensioneResponse;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.model.*;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.service._interface.LuogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service per gestire tutti i metodi inerenti al luogo e alle coordinate dei markers.
 */
@Service
@RequiredArgsConstructor
public class LuogoServiceImpl implements LuogoService {

    private final EventoRepository eventoRepository;

    /**
     * Metodo che, per ogni evento sul database, prende le coordinate dei markers.
     * @return Lista di DTO con i dati degli eventi comprese le coordinate.
     */
    @Override
    public List<EventoResponse> getAllMarkerCoordinates() {

        //Prendo tutti gli eventi presenti sul database.
        List<Evento> eventi = eventoRepository.findAll();

        List<EventoResponse> response = new ArrayList<>();

        //Per ogni evento futuro, aggiungo i dati al DTO.
        for(Evento evento : eventi) {
            if(evento.getDataInizio().isAfter(LocalDateTime.now())) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    /**
     * Metodo che, per ogni evento organizzato da un dato organizzatore, prende le coordinate dei markers.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli eventi comprese le coordinate.
     */
    @Override
    public List<EventoResponse> getAllMarkerCoordinatesByOrganizzatore(Long organizzatoreId) {

        //L'id dev'essere maggiore di 0.
        if(organizzatoreId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Prendo tutti gli eventi organizzati da un dato organizzatore.
        List<Evento> eventi = eventoRepository.findAllByOrganizzatoreUserId(organizzatoreId);

        List<EventoResponse> response = new ArrayList<>();

        //Per ogni evento, aggiungo i dati al DTO.
        for(Evento evento : eventi) {
            buildCoordinatesResponse(response, evento);
        }

        return response;
    }

    /**
     * Metodo che controlla se i marker si trovano all'interno di un poligono di n vertici disegnato dall'utente.
     * @param request Lista di coordinate dei vertici del poligono.
     * @return Lista di DTO con i dati degli eventi all'interno del poligono disegnato.
     */
    @Override
    public List<EventoResponse> coordinateDentroPoligono(List<PuntoPoligono> request) {

        //Controllo la validità delle coordinate.
        for(PuntoPoligono punto : request) {
            if(punto.getLat().isNaN() || punto.getLat().isInfinite() ||
                    punto.getLng().isNaN() || punto.getLng().isInfinite()) {
                throw new BadRequestException("Coordinate non valide");
            }
        }

        //Prendo tutti gli eventi presenti sul database.
        List<Evento> eventi = eventoRepository.findAll();

        List<EventoResponse> response = new ArrayList<>();

        //Per ogni evento futuro all'interno del poligono disegnato dall'utente, aggiungo i dati al DTO.
        for(Evento evento : eventi) {
            if(isMarkerInsidePolygon(
                evento.getLuogo().getLat(),
                evento.getLuogo().getLng(),
                request) &&
                evento.getDataInizio().isAfter(LocalDateTime.now())) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    /**
     * Metodo che controlla se i marker di un dato organizzatore si trovano all'interno di un poligono di n vertici disegnato dall'utente.
     * @param request Lista di coordinate dei vertici del poligono.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli eventi all'interno del poligono disegnato.
     */
    @Override
    public List<EventoResponse> coordinateDentroPoligonoByOrganizzatore(List<PuntoPoligono> request, Long organizzatoreId) {

        //L'id dev'essere maggiore di 0.
        if(organizzatoreId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Controllo la validità delle coordinate.
        for(PuntoPoligono punto : request) {
            if(punto.getLat().isNaN() || punto.getLat().isInfinite() ||
                    punto.getLng().isNaN() || punto.getLng().isInfinite()) {
                throw new BadRequestException("Coordinate non valide");
            }
        }

        //Prendo tutti gli eventi organizzati da un dato organizzatore.
        List<Evento> eventi = eventoRepository.findAllByOrganizzatoreUserId(organizzatoreId);

        List<EventoResponse> response = new ArrayList<>();

        //Per ogni evento all'interno del poligono disegnato dall'utente, aggiungo i dati al DTO.
        for(Evento evento : eventi) {
            if(isMarkerInsidePolygon(
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng(),
                    request)) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    /**
     * Metodo che controlla se i marker si trovano all'interno di una circonferenza disegnata dall'utente.
     * @param request DTO con le coordinate del centro e il raggio della circonferenza.
     * @return Lista di DTO con i dati degli eventi all'interno della circonferenza disegnata.
     */
    @Override
    public List<EventoResponse> coordinateDentroCirconferenza(DatiCirconferenza request) {

        //Controllo la validità delle coordinate.
        if(request.getCentroLat().isNaN() || request.getCentroLat().isInfinite() ||
                request.getCentroLng().isNaN() || request.getCentroLng().isInfinite() ||
                request.getRaggio().isNaN() || request.getRaggio().isInfinite()) {
            throw new BadRequestException("Coordinate non valide");
        }

        //Prendo tutti gli eventi presenti sul database.
        List<Evento> eventi = eventoRepository.findAll();

        List<EventoResponse> response = new ArrayList<>();

        //Per ogni evento futuro all'interno della circonferenza disegnata dall'utente, aggiungo i dati al DTO.
        for(Evento evento : eventi) {
            if(isMarkerInsideCircumference(
                evento.getLuogo().getLat(),
                evento.getLuogo().getLng(),
                request) &&
                evento.getDataInizio().isAfter(LocalDateTime.now())) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    /**
     * Metodo che controlla se i marker di un dato organizzatore si trovano all'interno di una circonferenza disegnata dall'utente.
     * @param request DTO con le coordinate del centro e il raggio della circonferenza.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli eventi all'interno della circonferenza disegnata.
     */
    @Override
    public List<EventoResponse> coordinateDentroCirconferenzaByOrganizzatore(DatiCirconferenza request, Long organizzatoreId) {

        //L'id dev'essere maggiore di 0.
        if(organizzatoreId < 1) {
            throw new BadRequestException("Id non valido");
        }

        //Controllo la validità delle coordinate.
        if(request.getCentroLat().isNaN() || request.getCentroLat().isInfinite() ||
                request.getCentroLng().isNaN() || request.getCentroLng().isInfinite() ||
                request.getRaggio().isNaN() || request.getRaggio().isInfinite()) {
            throw new BadRequestException("Coordinate non valide");
        }

        //Prendo tutti gli eventi organizzati da un dato organizzatore.
        List<Evento> eventi = eventoRepository.findAllByOrganizzatoreUserId(organizzatoreId);

        List<EventoResponse> response = new ArrayList<>();

        //Per ogni evento all'interno della circonferenza disegnata dall'utente, aggiungo i dati al DTO.
        for(Evento evento : eventi) {
            if(isMarkerInsideCircumference(
                    evento.getLuogo().getLat(),
                    evento.getLuogo().getLng(),
                    request)) {

                buildCoordinatesResponse(response, evento);
            }
        }

        return response;
    }

    /**
     * Metodo condiviso da tutti gli endpoint sovrastanti. Costruisce semplicemente la lista di DTO da ritornare al client.
     * @param response Lista di DTO da popolare con i dati richiesti.
     * @param evento Evento da cui prendere i dati da aggiungere alla lista.
     */
    private void buildCoordinatesResponse(List<EventoResponse> response, Evento evento) {

        List<String> usernameTuristi = new ArrayList<>();
        List<RecensioneResponse> recensioni = new ArrayList<>();

        //Per ogni turista iscritto all'evento, aggiungo l'username all'arraylist.
        for(User turista : evento.getIscritti()) {
            usernameTuristi.add(turista.getUsername());
        }

        //Per ogni recensione dell'evento, aggiungo dei dati alla lista di DTO.
        for(Recensione recensione : evento.getRecensioni()) {
            recensioni.add(new RecensioneResponse(
                    recensione.getUser().getUsername(),
                    recensione.getVoto(),
                    recensione.getTesto()
            ));
        }

        //Costruisco il DTO di risposta, aggiungendo anche le due liste compilate precedentemente.
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

    /**
     * Metodo che controlla se un marker è dentro un poligono disegnato dall'utente.
     * @param lat Latitudine del marker.
     * @param lng Longitudine del marker.
     * @param request Lista di DTO con le coordinate di tutti i vertici del poligono.
     * @return "true" se il marker è all'interno del poligono, "false" in caso contrario.
     */
    private boolean isMarkerInsidePolygon(float lat, float lng, List<PuntoPoligono> request) {

        //Flag utilizzato per tenere traccia se il marker è all'interno del poligono.
        boolean isInside = false;

        //Ciclo che scorre tutti i segmenti del poligono (quindi i vertici due a due). "i" è l'indice corrente e "j" è l'indice precedente.
        for(int i = 0, j = request.size() - 1; i < request.size(); j = i++) {

            //Per ogni coppia di punti consecutivi nel poligono, vengono estratte le coordinate.
            float latI = request.get(i).getLat();
            float lngI = request.get(i).getLng();

            float latJ = request.get(j).getLat();
            float lngJ = request.get(j).getLng();

            //Booleano che indica se il segmento orizzontale che passa per il marker interseca il segmento considerato.
            boolean intersect =
                    //Controlla la longitudine del marker è compresa tra le due longitudini degli estremi del segmento considerato.
                    lngI > lng != lngJ > lng &&
                    //Controllo se la latitudine del marker è compresa tra le due latitudini degli estremi del segmento considerato.
                    lat < ((latJ - latI) * (lng - lngI)) / (lngJ - lngI) + latI;

            /* Se intersect è true, significa che il raggio interseca il segmento, e viene invertito il valore di isInside.
            Questo è il cuore dell'algoritmo del "Ray Casting": se il raggio attraversa un numero dispari di segmenti del
            poligono, allora il punto è all'interno del poligono; altrimenti, è all'esterno. */
            if(intersect) {
                isInside = !isInside;
            }
        }

        //Il metodo restituisce il valore di isInside, che sarà true se il punto è all'interno del poligono e false altrimenti.
        return isInside;
    }

    /**
     * Metodo che controlla se un marker è dentro una circonferenza disegnata dall'utente.
     * @param lat Latitudine del marker.
     * @param lng Longitudine del marker.
     * @param request DTO con le coordinate del centro e il raggio della circonferenza.
     * @return "true" se il marker è all'interno della circonferenza, "false" in caso contrario.
     */
    private boolean isMarkerInsideCircumference(float lat, float lng, DatiCirconferenza request) {

        //Calcolo la distanza in metri tra le coordinate del marker e le coordinate del centro della circonferenza.
        float distanzaDalCentroAlMarker = distanceInMetersBetweenEarthCoordinates(
                lat, lng, request.getCentroLat(), request.getCentroLng());

        //Se la distanza appena calcolata è minore del raggio, vuol dire che il marker si trova all'interno della circonferenza.
        return distanzaDalCentroAlMarker <= request.getRaggio();
    }

    /**
     * Metodo che calcola la distanza in metri tra due punti sulla Terra utilizzando le loro latitudini e longitudini in gradi.
     * Viene utilizzata la formula dell'emisenoverso (o formula di Harvesine): formula della trigonometria sferica utile
     * per calcolare la distanza tra due punti sulla superficie di una sfera, come la Terra.
     * @param markerLat Latitudine del marker.
     * @param markerLng Longitudine del marker.
     * @param centroLat Latitudine del centro della circonferenza.
     * @param centroLng Latitudine del centro della circonferenza.
     * @return Distanza in km tra le due coordinate terrestri.
     */
    private float distanceInMetersBetweenEarthCoordinates(float markerLat, float markerLng, float centroLat, float centroLng) {

        //Raggio della terra in metri.
        final float RAGGIO_TERRA_METRI = 6371000;

        //Differenza tra le latitudini e le longitudini dei due punti in radianti.
        float differenzaLat = degreesToRadians(centroLat - markerLat);
        float differenzaLng = degreesToRadians(centroLng - markerLng);

        //Trasformo in radianti le latitudini dei due punti, utili successivamente.
        markerLat = degreesToRadians(markerLat);
        centroLat = degreesToRadians(centroLat);

        /* Valore intermedio calcolato secondo la formula di Haversine.
        Questo valore rappresenta la "haversine" dell'angolo centrale tra i
        due punti sulla superficie della Terra. La haversine è una funzione
        trigonometrica che coinvolge il seno e il coseno degli angoli. */
        float harv = (float)
                (Math.sin(differenzaLat / 2) * Math.sin(differenzaLat / 2) +
                Math.sin(differenzaLng / 2) * Math.sin(differenzaLng / 2) *
                Math.cos(markerLat) * Math.cos(centroLat));

        /* Distanza angolare tra i due punti sulla superficie della Terra.
        Questa distanza angolare è la metà dell'angolo tra i due punti
        quando si considera il centro della Terra come punto di origine. */
        float c = (float) (2 * Math.atan2(Math.sqrt(harv), Math.sqrt(1 - harv)));

        //Ritorno la distanza in metri tra i due punti sulla Terra.
        return RAGGIO_TERRA_METRI * c;
    }

    /**
     * Metodo che semplicemente trasforma un angolo da gradi in radianti tramite l'apposita formula.
     * @param gradi Valore dell'angolo in gradi.
     * @return Valore dell'angolo in radianti.
     */
    private float degreesToRadians(float gradi) {

        return (float) ((gradi * Math.PI) / 180);
    }

    /**
     * Ricevuti in ingresso data di inizio e data di fine di un evento, restituisce lo stato basato su data e ora attuali.
     * @param dataInizio Data di inizio dell'evento.
     * @param dataFine Data di fine dell'evento.
     * @return Lo stato dell'evento (passato, presente o futuro).
     */
    private Stato getStatoEvento(LocalDateTime dataInizio, LocalDateTime dataFine) {

        return dataInizio.isAfter(LocalDateTime.now()) ? Stato.FUTURO
                : dataFine.isBefore(LocalDateTime.now()) ? Stato.PASSATO
                : Stato.PRESENTE;
    }
}
