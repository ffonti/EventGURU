package it.polimi.iswpf.unit.controller;

import it.polimi.iswpf.controller.RecensioneController;
import it.polimi.iswpf.dto.request.InviaRecensioneRequest;
import it.polimi.iswpf.service.implementation.RecensioneServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecensioneControllerTest {

    @Mock
    RecensioneServiceImpl recensioneService;

    @InjectMocks
    RecensioneController recensioneController;

    @Test
    void inviaRecensione() {

        InviaRecensioneRequest request = new InviaRecensioneRequest(3, "");

        assertAll(() -> recensioneController.inviaRecensione(request, "1", "1"));
    }

    @Test
    void getAllByEvento() {

        assertAll(() -> recensioneController.getAllByEvento("1"));
    }

    @Test
    void getRecensione() {

        assertAll(() -> recensioneController.getRecensione("1", ""));
    }
}