package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.exception.*;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service per gestire tutti i metodi inerenti all'user.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserData(Long userId) {
        if(userId < 1) {
            throw new IdNonValidoException();
        }

        Optional<User> userExists = userRepository.findByUserId(userId);

        if(userExists.isEmpty()) {
            throw new UtenteNonTrovatoException();
        }

        return userExists.get();
    }

    @Override
    public User updateUserData(Long userId, UpdateUserDataRequest request) {
        if(userId < 1) {
            throw new IdNonValidoException();
        }

        Optional<User> userExists = userRepository.findByUserId(userId);

        if(userExists.isEmpty()) {
            throw new UtenteNonTrovatoException();
        } else {
            User user = userExists.get();

            if(!request.getNome().isEmpty() && !request.getNome().isBlank()) {
                user.setNome(request.getNome());
            }

            if(!request.getCognome().isEmpty() && !request.getCognome().isBlank()) {
                user.setCognome(request.getCognome());
            }

            if(!request.getEmail().isEmpty() && !request.getEmail().isBlank()) {
                user.setEmail(request.getEmail());
            }

            if(!request.getUsername().isEmpty() &&
                !request.getUsername().isBlank() &&
                !request.getUsername().equals(user.getUsername())) {
                Optional<User> userWithUsername = userRepository.findByUsername(request.getUsername());

                if(userWithUsername.isPresent()) {
                    throw new UsernameRegistratoException();
                } else {
                    user.setUsername(request.getUsername());
                }
            }

            if(!request.getNuovaPassword().isEmpty() &&
                !request.getNuovaPassword().isBlank() &&
                !request.getVecchiaPassword().isEmpty() &&
                !request.getVecchiaPassword().isBlank()) {

                if(!passwordEncoder.matches(request.getVecchiaPassword(), user.getPassword())) {
                    throw new PasswordErrataException();
                }

                if(request.getVecchiaPassword().equals(request.getNuovaPassword())) {
                    throw new PasswordUgualiException();
                }

                user.setPassword(passwordEncoder.encode(request.getNuovaPassword()));
            }

            user.setIscrittoNewsletter(request.isIscrittoNewsletter());

            userRepository.save(user);

            return user;
        }
    }
}
