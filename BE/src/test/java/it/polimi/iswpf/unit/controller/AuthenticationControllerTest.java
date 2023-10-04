package it.polimi.iswpf.unit.controller;

import it.polimi.iswpf.controller.AuthenticationController;
import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.service.implementation.AuthenticationServiceImpl;
import it.polimi.iswpf.service.implementation.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    EmailServiceImpl emailService;

    @Mock
    AuthenticationServiceImpl authenticationService;

    @InjectMocks
    AuthenticationController authenticationController;

    @Test
    void register() {

        RegisterRequest request = new RegisterRequest(
                "Fabrizio",
                "Fontana",
                "fabriziofontana02@gmail.com",
                "fonti",
                "password",
                "TURISTA");

        assertAll(() -> authenticationController.register(request));
    }

    @Test
    void authenticate() {

        LoginRequest request = new LoginRequest(
                "fonti",
                "password");

        when(authenticationService.login(request)).thenReturn(new LoginResponse(
                1L, "", "", "", Ruolo.TURISTA, "", false, "", ""
        ));

        assertAll(() -> authenticationController.authenticate(request));
    }

    @Test
    void recuperaPassword() {

        assertAll(() -> authenticationController.recuperaPassword("fabriziofontana02@gmail.com"));
    }
}