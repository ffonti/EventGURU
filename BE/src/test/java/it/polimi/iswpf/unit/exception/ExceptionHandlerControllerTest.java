package it.polimi.iswpf.unit.exception;

import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.exception.controller.ExceptionHandlerController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerTest {

    @InjectMocks
    ExceptionHandlerController controller;

    @Test
    void handleBadRequestException() {

        assertAll(() -> controller.handleBadRequestException(new BadRequestException("Bad request exception test")));
    }

    @Test
    void handleForbiddenException() {

        assertAll(() -> controller.handleForbiddenException(new ForbiddenException("Forbidden exception test")));
    }

    @Test
    void handleNotFoundException() {

        assertAll(() -> controller.handleNotFoundException(new NotFoundException("Not found exception test")));
    }

    @Test
    void handleConflictException() {

        assertAll(() -> controller.handleConflictException(new ConflictException("Conflict exception test")));
    }

    @Test
    void handleInternalServerErrorException() {

        assertAll(() -> controller.handleInternalServerErrorException(new InternalServerErrorException("Internal server error exception test")));
    }
}