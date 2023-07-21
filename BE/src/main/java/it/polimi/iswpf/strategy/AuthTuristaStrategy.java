package it.polimi.iswpf.strategy;

import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;

public class AuthTuristaStrategy implements AuthStrategy {

    @Override
    public Ruolo getRuolo() {
        return Ruolo.TURISTA;
    }

    @Override
    public User register(RegisterRequest request) {
        return null;
    }
}
