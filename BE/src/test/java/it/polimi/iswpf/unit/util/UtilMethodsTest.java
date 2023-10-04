package it.polimi.iswpf.unit.util;

import it.polimi.iswpf.util.UtilMethods;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UtilMethodsTest {

    @InjectMocks
    UtilMethods utilMethods;

    @Test
    void getInstance() {

        assertAll(UtilMethods::getInstance);
    }

    @Test
    void dateToString() {

        assertAll(() -> utilMethods.dateToString(LocalDateTime.now()));
    }
}