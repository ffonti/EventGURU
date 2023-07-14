package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(@NonNull RegisterRequest request) throws Exception {

        final String nome = request.getNome().trim();
        final String cognome = request.getCognome().trim();
        final String email = request.getEmail().trim().toLowerCase();
        final String username = request.getUsername().trim().toLowerCase();
        final String password = request.getPassword();

        checkData(List.of(nome, cognome, email, username, password));

        User user = User
                .builder()
                .nome(nome)
                .cognome(cognome)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .ruolo(Ruolo.USER)
                .iscritto(false)
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(@NonNull LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername().trim().toLowerCase(),
                    request.getPassword()
            )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String jwt = jwtServiceImpl.generateToken(user);

        return LoginResponse
                .builder()
                .token(jwt)
                .build();
    }

    public void checkData(List<String> dataList) throws Exception {
        for(String data : dataList) {
            if(data.isEmpty() || data.isBlank()) {
                throw new Exception("Inserire tutti i campi");
            }
        }
    }
}