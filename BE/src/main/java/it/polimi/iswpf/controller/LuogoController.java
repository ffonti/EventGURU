package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.response.MarkerCoordinatesResponse;
import it.polimi.iswpf.service._interface.LuogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
