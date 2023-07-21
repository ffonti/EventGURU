package it.polimi.iswpf.strategy;

import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;

public class AuthOrganizzatoreStrategy implements AuthStrategy {

    @Override
    public Ruolo getRuolo() {
        return Ruolo.ORGANIZZATORE;
    }

    @Override
    public User register(RegisterRequest request) {
        return null;
    }
}
