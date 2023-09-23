package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.dto.response.MarkerCoordinatesResponse;
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
    public ResponseEntity<List<MarkerCoordinatesResponse>> getAllMarkerCoordinates() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.getAllMarkerCoordinates());
    }

    @PostMapping("/coordinateDentroPoligono")
    public ResponseEntity<List<MarkerCoordinatesResponse>> coordinateDentroPoligono(
            @RequestBody List<PuntoPoligono> request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroPoligono(request));
    }

    @PostMapping("/coordinateDentroCirconferenza")
    public ResponseEntity<List<MarkerCoordinatesResponse>> coordinateDentroCirconferenza(
            @RequestBody List<DatiCirconferenza> request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(luogoService.coordinateDentroCirconferenza(request));
    }
}
