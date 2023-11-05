package it.polimi.iswpf.unit.service.implementation;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.ConflictException;
import it.polimi.iswpf.exception.InternalServerErrorException;
import it.polimi.iswpf.model.entity.User;
import it.polimi.iswpf.model.repository.UserRepository;
import it.polimi.iswpf.service.implementation.AuthenticationServiceImpl;
import it.polimi.iswpf.security.service.implementation.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtServiceImpl jwtService;

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @Test
    void registerThrowsRuoloNonValido() {

        RegisterRequest request = new RegisterRequest(
                "", "", "", "", "", "ADMIN"
        );

        assertThrows(BadRequestException.class,
                () -> authenticationService.register(request));
    }

    @Test
    void registerThrowsUsernameGiaRegistrato() {

        RegisterRequest request = new RegisterRequest(
                "Fabrizio",
                "Fontana",
                "fabriziofontana02@gmail.com",
                "fonti",
                "password",
                "TURISTA"
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        assertThrows(ConflictException.class,
                () -> authenticationService.register(request));
    }

    @Test
    void registerThrowsRuoloNonValidoSwitch() {

        RegisterRequest request = new RegisterRequest(
                "Fabrizio",
                "Fontana",
                "fabriziofontana02@gmail.com",
                "fonti",
                "password",
                "NO"
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> authenticationService.register(request));
    }

    @Test
    void registerThrowsCompilareTuttiCampi() {

        RegisterRequest request = new RegisterRequest(
                "Fabrizio",
                "Fontana",
                "fabriziofontana02@gmail.com",
                "",
                "password",
                "TURISTA"
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> authenticationService.register(request));
    }

    @Test
    void registerThrowsRegistrazioneNonRiuscita() {

        RegisterRequest request = new RegisterRequest(
                "Fabrizio",
                "Fontana",
                "fabriziofontana02@gmail.com",
                "fonti",
                "password",
                "ORGANIZZATORE"
        );

        when(userRepository.findByUsername("fonti")).thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class,
                () -> authenticationService.register(request));
    }

    @Test
    void loginSuccessful() {

        LoginRequest request = new LoginRequest("fonti", "password");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        assertAll(() -> authenticationService.login(request));
    }

    @Test
    void putJwtInHttpHeadersSuccessful() {

        assertAll(() -> authenticationService.putJwtInHttpHeaders("jwt"));
    }
}