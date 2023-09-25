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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/luogo")
public class LuogoController {

    private final LuogoService luogoService;

    @GetMapping("/getAllMarkerCoordinates")
    public ResponseEntity<List<EventoResponse>> getAllMarkerCoordinates() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.getAllMarkerCoordinates());
    }

    @GetMapping("/getAllMarkerCoordinates/{organizzatoreId}")
    public ResponseEntity<List<EventoResponse>> getAllMarkerCoordinatesByOrganizzatore(@PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.getAllMarkerCoordinatesByOrganizzatore(Long.parseLong(organizzatoreId)));
    }

    @PostMapping("/coordinateDentroPoligono")
    public ResponseEntity<List<EventoResponse>> coordinateDentroPoligono(
            @RequestBody List<PuntoPoligono> request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroPoligono(request));
    }

    @PostMapping("/coordinateDentroPoligono/{organizzatoreId}")
    public ResponseEntity<List<EventoResponse>> coordinateDentroPoligonoByOrganizzatore(
            @RequestBody List<PuntoPoligono> request,
            @PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroPoligonoByOrganizzatore(request, Long.parseLong(organizzatoreId)));
    }

    @PostMapping("/coordinateDentroCirconferenza")
    public ResponseEntity<List<EventoResponse>> coordinateDentroCirconferenza(
            @RequestBody DatiCirconferenza request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroCirconferenza(request));
    }

    @PostMapping("/coordinateDentroCirconferenza/{organizzatoreId}")
    public ResponseEntity<List<EventoResponse>> coordinateDentroCirconferenzaByOrganizzatore(
            @RequestBody DatiCirconferenza request,
            @PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroCirconferenzaByOrganizzatore(request, Long.parseLong(organizzatoreId)));
    }
}
