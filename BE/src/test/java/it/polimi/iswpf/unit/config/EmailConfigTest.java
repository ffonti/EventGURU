package it.polimi.iswpf.unit.config;

import it.polimi.iswpf.config.EmailConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailConfigTest {

    @InjectMocks
    EmailConfig emailConfig;

    @Test
    void getJavaMailSenderSuccessful() {

        assertAll(() -> emailConfig.getJavaMailSender());
    }

    @Test
    void factoryBeanSuccessful() {

        assertAll(() -> emailConfig.factoryBean());
    }
}