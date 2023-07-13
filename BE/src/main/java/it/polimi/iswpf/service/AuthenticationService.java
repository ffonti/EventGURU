package it.polimi.iswpf.service;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.dto.response.RegisterResponse;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(@NonNull RegisterRequest request) {

        User user = User
                .builder()
                .nome(request.getNome().trim())
                .cognome(request.getCognome().trim())
                .email(request.getEmail().trim().toLowerCase())
                .username(request.getUsername().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .ruolo(Ruolo.USER)
                .iscritto(false)
                .build();

        userRepository.save(user);

        return RegisterResponse
                .builder()
                .msg("Registrazione completata")
                .build();
    }

    public LoginResponse login(@NonNull LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername().trim().toLowerCase(),
                    request.getPassword()
            )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String jwt = jwtService.generateToken(user);

        return LoginResponse
                .builder()
                .token(jwt)
                .build();
    }
}
