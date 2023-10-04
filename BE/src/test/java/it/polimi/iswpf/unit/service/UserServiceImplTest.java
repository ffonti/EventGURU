package it.polimi.iswpf.unit.service;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getUserDataThrowsNonValidId() {

        assertThrows(BadRequestException.class,
                () -> userService.getUserData(0L));
    }

    @Test
    void getUserDataThrowsUserNotFound() {

        when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class,
                () -> userService.getUserData(1L));
    }

    @Test
    void getUserDataSuccessful() {

        Optional<User> user = Optional.of(new UserBuilder()
                .userId(1L)
                .build());

        when(userRepository.findByUserId(1L)).thenReturn(user);

        assertAll(() -> userService.getUserData(1L));
    }

    @Test
    void updateUserDataThrowsNonValidId() {

        assertThrows(BadRequestException.class,
                () -> userService.updateUserData(0L, null));
    }

    @Test
    void updateUserDataThrowsUserNotFound() {

        when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class,
                () -> userService.updateUserData(1L, null));
    }

    @Test
    void updateUserDataThrowsUsernameAlreadyRegistered() {

        Optional<User> user = Optional.of(new UserBuilder()
                .userId(1L)
                .nome("Fabrizio")
                .cognome("Fontana")
                .email("fabriziofontana02@gmail.com")
                .username("fonti")
                .password("password")
                .dataCreazione(LocalDateTime.now())
                .ruolo(Ruolo.TURISTA)
                .iscrittoNewsletter(false)
                .build());

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "Francesco",
                "Bianchi",
                "francescobianchi@gmail.com",
                "ciccio",
                "password",
                "nuovaPassword",
                true);

        when(userRepository.findByUserId(1L)).thenReturn(user);

        when(userRepository.findByUsername(eq("ciccio"))).thenReturn(Optional.of(new User()));

        assertThrows(ConflictException.class,
                () -> userService.updateUserData(1L, request));
    }

    @Test
    void updateUserDataThrowsPasswordErrata() {

        Optional<User> user = Optional.of(new UserBuilder()
                .userId(1L)
                .nome("Fabrizio")
                .cognome("Fontana")
                .email("fabriziofontana02@gmail.com")
                .username("fonti")
                .password("password")
                .dataCreazione(LocalDateTime.now())
                .ruolo(Ruolo.TURISTA)
                .iscrittoNewsletter(false)
                .build());

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "Francesco",
                "Bianchi",
                "francescobianchi@gmail.com",
                "ciccio",
                "passwordss",
                "nuovaPassword",
                true);

        when(userRepository.findByUserId(1L)).thenReturn(user);

        assertThrows(BadRequestException.class,
                () -> userService.updateUserData(1L, request));
    }

    @Test
    void updateUserDataThrowsPasswordUguale() {

        Optional<User> user = Optional.of(new UserBuilder()
                .userId(1L)
                .nome("Fabrizio")
                .cognome("Fontana")
                .email("fabriziofontana02@gmail.com")
                .username("fonti")
                .password("password")
                .dataCreazione(LocalDateTime.now())
                .ruolo(Ruolo.TURISTA)
                .iscrittoNewsletter(false)
                .build());

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "Francesco",
                "Bianchi",
                "francescobianchi@gmail.com",
                "ciccio",
                "password",
                "password",
                true);

        when(userRepository.findByUserId(1L)).thenReturn(user);

        when(passwordEncoder.matches(request.getVecchiaPassword(), user.get().getPassword())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> userService.updateUserData(1L, request));
    }

    @Test
    void updateUserDataSuccessful() {

        Optional<User> user = Optional.of(new UserBuilder()
                .userId(1L)
                .nome("Fabrizio")
                .cognome("Fontana")
                .email("fabriziofontana02@gmail.com")
                .username("fonti")
                .password("password")
                .dataCreazione(LocalDateTime.now())
                .ruolo(Ruolo.TURISTA)
                .iscrittoNewsletter(false)
                .build());

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "Francesco",
                "Bianchi",
                "francescobianchi@gmail.com",
                "ciccio",
                "password",
                "nuovaPassword",
                true);

        when(userRepository.findByUserId(1L)).thenReturn(user);

        when(passwordEncoder.matches(request.getVecchiaPassword(), user.get().getPassword())).thenReturn(true);

        assertAll(() -> userService.updateUserData(1L, request));
    }

    @Test
    void adminUpdateUserDataThrowsNonValidUsername() {

        assertThrows(BadRequestException.class,
                () -> userService.adminUpdateUserData("", null));
    }

    @Test
    void adminUpdateUserDataThrowsUserNotFound() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class,
                () -> userService.adminUpdateUserData("fonti", null));
    }

    @Test
    void adminUpdateUserDataSuccessful() {

        Optional<User> user = Optional.of(new UserBuilder()
                .userId(1L)
                .nome("Fabrizio")
                .cognome("Fontana")
                .email("fabriziofontana02@gmail.com")
                .username("fonti")
                .password("password")
                .dataCreazione(LocalDateTime.now())
                .ruolo(Ruolo.TURISTA)
                .iscrittoNewsletter(false)
                .build());

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "Francesco",
                "Bianchi",
                "francescobianchi@gmail.com",
                "ciccio",
                "password",
                "nuovaPassword",
                true);

        when(userRepository.findByUsername("fonti")).thenReturn(user);

        when(passwordEncoder.matches(request.getVecchiaPassword(), user.get().getPassword())).thenReturn(true);

        assertAll(() -> userService.adminUpdateUserData("fonti", request));
    }

    @Test
    void deleteAccountThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> userService.deleteAccount(0L));
    }

    @Test
    void deleteAccountThrowsUtenteNonTrovato() {

        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.deleteAccount(1L));
    }

    @Test
    void deleteAccountSuccessful() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(new User()));

        assertThrows(InternalServerErrorException.class,
                () -> userService.deleteAccount(1L));
    }

    @Test
    void getAdminUserDataThrowsUsernameNonValido() {

        assertThrows(BadRequestException.class,
                () -> userService.getAdminUserData(""));
    }

    @Test
    void getAdminUserDataThrowsUtenteNonTrovato() {

        when(userRepository.findByUsername(eq("fonti"))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getAdminUserData("fonti"));
    }

    @Test
    void getAdminUserDataSuccessful() {

        when(userRepository.findByUsername(eq("fonti"))).thenReturn(Optional.of(new User()));

        assertAll(() -> userService.getAdminUserData("fonti"));
    }

    @Test
    void adminDeleteAccountThrowsUsernameNonValido() {

        assertThrows(BadRequestException.class,
                () -> userService.adminDeleteAccount(""));
    }

    @Test
    void adminDeleteAccountThrowsUtenteNonTrovato() {

        when(userRepository.findByUsername(eq("fonti"))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.adminDeleteAccount("fonti"));
    }

    @Test
    void adminDeleteAccountThrowsErroreEliminazione() {

        when(userRepository.findByUsername(eq("fonti"))).thenReturn(Optional.of(new User()));

        assertThrows(InternalServerErrorException.class,
                () -> userService.adminDeleteAccount("fonti"));
    }

    @Test
    void getAllOrganizzatoriThrowsOrganizzatoriNonTrovati() {

        when(userRepository.findAllByRuolo(eq(Ruolo.ORGANIZZATORE))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getAllOrganizzatori());
    }

    @Test
    void getAllOrganizzatoriSuccessful() {

        when(userRepository.findAllByRuolo(eq(Ruolo.ORGANIZZATORE)))
                .thenReturn(Optional.of(List.of(new UserBuilder()
                                .userId(1L)
                                .username("fonti")
                                .nome("Fabrizio")
                                .cognome("Fontana")
                                .dataCreazione(LocalDateTime.now())
                                .eventi(List.of(new EventoBuilder()
                                        .dataInizio(LocalDateTime.now().plusHours(1))
                                        .build()))
                        .build())));

        assertAll(() -> userService.getAllOrganizzatori());
    }

    @Test
    void getAllByRuoloThrowsRuoloNonValido() {

        assertThrows(BadRequestException.class,
                () -> userService.getAllByRuolo(""));
    }

    @Test
    void getAllByRuoloThrowsRuoloNonValidoSwitch() {

        assertThrows(BadRequestException.class,
                () -> userService.getAllByRuolo("ADMIN"));
    }

    @Test
    void getAllByRuoloThrowsUtentiNonTrovati() {

        when(userRepository.findAllByRuolo(Ruolo.TURISTA)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getAllByRuolo("TURISTA"));
    }

    @Test
    void getAllByRuoloSuccessful() {

        when(userRepository.findAllByRuolo(Ruolo.ORGANIZZATORE)).thenReturn(Optional.of(List.of(new User())));

        assertAll(() -> userService.getAllByRuolo("ORGANIZZATORE"));
    }

    @Test
    void seguiOrganizzatoreThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> userService.seguiOrganizzatore(1L, 0L));
    }

    @Test
    void seguiOrganizzatoreThrowsUtenteNonTrovato() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.seguiOrganizzatore(1L, 2L));
    }

    @Test
    void seguiOrganizzatoreThrowsPermessiNonAdatti() {

        User organizzatore = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(organizzatore));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertThrows(ForbiddenException.class,
                () -> userService.seguiOrganizzatore(1L, 2L));
    }

    @Test
    void seguiOrganizzatoreThrowsTuristaSegueGiaOrganizzatore() {

        User organizzatore = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .seguiti(List.of(organizzatore))
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(organizzatore));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertThrows(BadRequestException.class,
                () -> userService.seguiOrganizzatore(1L, 2L));
    }

    @Test
    void seguiOrganizzatoreSuccessful() {

        List<User> seguiti = new ArrayList<>();

        User organizzatore = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .seguiti(seguiti)
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(organizzatore));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertAll(() -> userService.seguiOrganizzatore(1L, 2L));
    }

    @Test
    void getOrganizzatoriSeguitiThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> userService.getOrganizzatoriSeguiti(0L));
    }

    @Test
    void getOrganizzatoriSeguitiThrowsUtenteNonTrovato() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getOrganizzatoriSeguiti(1L));
    }

    @Test
    void getOrganizzatoriSeguitiThrowsPermessiNonAdatti() {

        User turista = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(turista));

        assertThrows(ForbiddenException.class,
                () -> userService.getOrganizzatoriSeguiti(1L));
    }

    @Test
    void getOrganizzatoriSeguitiSuccessful() {

        List<User> seguiti = List.of(
                new UserBuilder()
                        .username("ciccio")
                        . build()
        );

        User turista = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.TURISTA)
                .seguiti(seguiti)
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(turista));

        assertAll(() -> userService.getOrganizzatoriSeguiti(1L));
    }

    @Test
    void smettiSeguireOrganizzatoreThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> userService.smettiSeguireOrganizzatore(0L, 1L));
    }

    @Test
    void smettiSeguireOrganizzatoreThrowsUtenteNonTrovato() {

        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.smettiSeguireOrganizzatore(1L, 2L));
    }

    @Test
    void smettiSeguireOrganizzatoreThrowsPermessiNonAdatti() {

        User organizzatore = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(organizzatore));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertThrows(ForbiddenException.class,
                () -> userService.smettiSeguireOrganizzatore(1L, 2L));
    }

    @Test
    void smettiSeguireOrganizzatoreThrowsTuristaNonSegueOrganizzatore() {

        User organizzatore = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .seguiti(List.of())
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(organizzatore));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertThrows(BadRequestException.class,
                () -> userService.smettiSeguireOrganizzatore(1L, 2L));
    }

    @Test
    void smettiSeguireOrganizzatoreSuccessful() {

        List<User> seguiti = new ArrayList<>();

        User organizzatore = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        seguiti.add(organizzatore);

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .seguiti(seguiti)
                .build();

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(organizzatore));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertAll(() -> userService.smettiSeguireOrganizzatore(1L, 2L));
    }
}