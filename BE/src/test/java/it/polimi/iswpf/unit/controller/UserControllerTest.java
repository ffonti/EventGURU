package it.polimi.iswpf.unit.controller;

import it.polimi.iswpf.controller.UserController;
import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;

    @Test
    void getUserData() {

        assertAll(() -> userController.getUserData("1"));
    }

    @Test
    void updateUserData() {

        UpdateUserDataRequest request =
                new UpdateUserDataRequest("", "", "", "", "", "", false);

        assertAll(() -> userController.updateUserData(request, "1"));
    }

    @Test
    void adminUpdateUserData() {

        UpdateUserDataRequest request =
                new UpdateUserDataRequest("", "", "", "", "", "", false);

        assertAll(() -> userController.adminUpdateUserData(request, "1"));
    }

    @Test
    void deleteAccount() {

        assertAll(() -> userController.deleteAccount("1"));
    }

    @Test
    void getAdminUserData() {

        assertAll(() -> userController.getAdminUserData(""));
    }

    @Test
    void adminDeleteAccount() {

        assertAll(() -> userController.adminDeleteAccount(""));
    }

    @Test
    void getAllOrganizzatori() {

        assertAll(() -> userController.getAllOrganizzatori());
    }

    @Test
    void getAllByRuolo() {

        assertAll(() -> userController.getAllByRuolo(""));
    }

    @Test
    void seguiOrganizzatore() {

        assertAll(() -> userController.seguiOrganizzatore("1", "2"));
    }

    @Test
    void getOrganizzatoriSeguiti() {

        assertAll(() -> userController.getOrganizzatoriSeguiti("1"));
    }

    @Test
    void smettiSeguireOrganizzatore() {

        assertAll(() -> userController.smettiSeguireOrganizzatore("2", "1"));
    }
}