package it.polimi.iswpf.strategy;

import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;

public interface AuthStrategy {

    Ruolo getRuolo();

    User register(RegisterRequest request);
}
