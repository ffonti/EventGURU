package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.EventoResponse;
import it.polimi.iswpf.service.implementation.LuogoServiceImpl;


import java.util.List;

/**
 * Interfaccia che contiene le firme dei metodi del service che gestisce luoghi e coordinate.
 * Implementazione -> {@link LuogoServiceImpl}.
 */
public interface LuogoService {

    /**
     * Metodo che, per ogni evento sul database, prende le coordinate dei markers.
     * @return Lista di DTO con i dati degli eventi comprese le coordinate.
     */
    List<EventoResponse> getAllMarkerCoordinates();

    /**
     * Metodo che, per ogni evento organizzato da un dato organizzatore, prende le coordinate dei markers.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli eventi comprese le coordinate.
     */
    List<EventoResponse> getAllMarkerCoordinatesByOrganizzatore(Long organizzatoreId);

    /**
     * Metodo che controlla se i marker si trovano all'interno di un poligono di n vertici disegnato dall'utente.
     * @param request Lista di coordinate dei vertici del poligono.
     * @return Lista di DTO con i dati degli eventi all'interno del poligono disegnato.
     */
    List<EventoResponse> coordinateDentroPoligono(List<PuntoPoligono> request);

    /**
     * Metodo che controlla se i marker di un dato organizzatore si trovano all'interno di un poligono di n vertici disegnato dall'utente.
     * @param request Lista di coordinate dei vertici del poligono.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli eventi all'interno del poligono disegnato.
     */
    List<EventoResponse> coordinateDentroPoligonoByOrganizzatore(List<PuntoPoligono> request, Long organizzatoreId);

    /**
     * Metodo che controlla se i marker si trovano all'interno di una circonferenza disegnata dall'utente.
     * @param request DTO con le coordinate del centro e il raggio della circonferenza.
     * @return Lista di DTO con i dati degli eventi all'interno della circonferenza disegnata.
     */
    List<EventoResponse> coordinateDentroCirconferenza(DatiCirconferenza request);

    /**
     * Metodo che controlla se i marker di un dato organizzatore si trovano all'interno di una circonferenza disegnata dall'utente.
     * @param request DTO con le coordinate del centro e il raggio della circonferenza.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli eventi all'interno della circonferenza disegnata.
     */
    List<EventoResponse> coordinateDentroCirconferenzaByOrganizzatore(DatiCirconferenza request, Long organizzatoreId);
}
