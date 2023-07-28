package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.exception.IdNonValidoException;
import it.polimi.iswpf.exception.UtenteNonTrovatoException;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service per gestire tutti i metodi inerenti all'user.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
