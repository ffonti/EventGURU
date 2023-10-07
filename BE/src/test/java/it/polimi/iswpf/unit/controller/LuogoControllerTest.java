package it.polimi.iswpf.unit.controller;

import it.polimi.iswpf.controller.LuogoController;
import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.service.implementation.LuogoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LuogoControllerTest {

    @Mock
    LuogoServiceImpl luogoService;

    @InjectMocks
    LuogoController luogoController;

    @Test
    void getAllMarkerCoordinates() {

        assertAll(() -> luogoController.getAllMarkerCoordinates());
    }

    @Test
    void getAllMarkerCoordinatesByOrganizzatore() {

        assertAll(() -> luogoController.getAllMarkerCoordinatesByOrganizzatore("1"));
    }

    @Test
    void coordinateDentroPoligono() {

        List<PuntoPoligono> request = new ArrayList<>();

        assertAll(() -> luogoController.coordinateDentroPoligono(request));
    }

    @Test
    void coordinateDentroPoligonoByOrganizzatore() {

        List<PuntoPoligono> request = new ArrayList<>();

        assertAll(() -> luogoController.coordinateDentroPoligonoByOrganizzatore(request, "1"));
    }

    @Test
    void coordinateDentroCirconferenza() {

        DatiCirconferenza request = new DatiCirconferenza(0F, 0F, 0F);

        assertAll(() -> luogoController.coordinateDentroCirconferenza(request));
    }

    @Test
    void coordinateDentroCirconferenzaByOrganizzatore() {

        DatiCirconferenza request = new DatiCirconferenza(0F, 0F, 0F);

        assertAll(() -> luogoController.coordinateDentroCirconferenzaByOrganizzatore(request, "1"));
    }
}