package it.polimi.iswpf.unit.config;

import it.polimi.iswpf.config.ApplicationConfig;
import it.polimi.iswpf.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    AuthenticationConfiguration config;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ApplicationConfig applicationConfig;

    @Test
    void userDetailsServiceSuccessful() {

        assertAll(() -> applicationConfig.userDetailsService());
    }

    @Test
    void authenticationProviderSuccessful() {

        assertAll(() -> applicationConfig.authenticationProvider());
    }

    @Test
    void authenticationManagerSuccessful() {

        assertAll(() -> applicationConfig.authenticationManager(config));
    }

    @Test
    void passwordEncoderSuccessful() {

        assertAll(() -> applicationConfig.passwordEncoder());
    }
}