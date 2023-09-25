package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.EventoResponse;
import it.polimi.iswpf.service._interface.LuogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per il luogo e tutto ciò che riguarda le coordinate dei markers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/luogo")
public class LuogoController {

    private final LuogoService luogoService;

    /**
     * Metodo per prendere le informazioni degli eventi, comprese le coordinate dei markers.
     * @return Lista di DTO con informazioni sugli eventi.
     */
    @GetMapping("/getAllMarkerCoordinates")
    public ResponseEntity<List<EventoResponse>> getAllMarkerCoordinates() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.getAllMarkerCoordinates());
    }

    /**
     * Metodo per prendere le informazioni degli eventi, comprese le coordinate
     * dei markers, creati da un dato organizzatore.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con informazioni sugli eventi.
     */
    @GetMapping("/getAllMarkerCoordinates/{organizzatoreId}")
    public ResponseEntity<List<EventoResponse>> getAllMarkerCoordinatesByOrganizzatore(
            @PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService
                        .getAllMarkerCoordinatesByOrganizzatore(Long.parseLong(organizzatoreId)));
    }

    /**
     * Metodo per verificare, dati dei markers, se sono all'interno di un poligono.
     * @param request Lista di DTO con le coordinate di ogni vertice del poligono.
     * @return Lista di DTO con i dati degli eventi dentro il poligono.
     */
    @PostMapping("/coordinateDentroPoligono")
    public ResponseEntity<List<EventoResponse>> coordinateDentroPoligono(
            @RequestBody List<PuntoPoligono> request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroPoligono(request));
    }

    /**
     * Metodo per verificare, dati dei markers di un organizzatore, se sono all'interno di un poligono.
     * @param request Lista di DTO con le coordinate di ogni vertice del poligono.
     * @param organizzatoreId Id univoco dell'organizzatore con il quale filtrare ulteriormente gli eventi.
     * @return Lista di DTO con i dati degli eventi dentro il poligono.
     */
    @PostMapping("/coordinateDentroPoligono/{organizzatoreId}")
    public ResponseEntity<List<EventoResponse>> coordinateDentroPoligonoByOrganizzatore(
            @RequestBody List<PuntoPoligono> request,
            @PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService
                        .coordinateDentroPoligonoByOrganizzatore(request, Long.parseLong(organizzatoreId)));
    }

    /**
     * Metodo per verificare, dati dei markers, se sono all'interno di una circonferenza.
     * @param request DTO con le coordinate del centro della circonferenza e il suo raggio.
     * @return Lista di DTO con i dati degli eventi dentro la circonferenza.
     */
    @PostMapping("/coordinateDentroCirconferenza")
    public ResponseEntity<List<EventoResponse>> coordinateDentroCirconferenza(
            @RequestBody DatiCirconferenza request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroCirconferenza(request));
    }

    /**
     * Metodo per verificare, dati dei markers di un organizzatore, se sono all'interno di una circonferenza.
     * @param request DTO con le coordinate del centro della circonferenza e il suo raggio.
     * @param organizzatoreId Id univoco dell'organizzatore con il quale filtrare ulteriormente gli eventi.
     * @return Lista di DTO con i dati degli eventi dentro la circonferenza.
     */
    @PostMapping("/coordinateDentroCirconferenza/{organizzatoreId}")
    public ResponseEntity<List<EventoResponse>> coordinateDentroCirconferenzaByOrganizzatore(
            @RequestBody DatiCirconferenza request,
            @PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService
                        .coordinateDentroCirconferenzaByOrganizzatore(request, Long.parseLong(organizzatoreId)));
    }
}
