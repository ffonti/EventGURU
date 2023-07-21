package it.polimi.iswpf.strategy;

import it.polimi.iswpf.dto.request.RegisterRequest;

public class AuthContext {

    private AuthStrategy authStrategy;

    public void setAuthStrategy(AuthStrategy authStrategy) {
        this.authStrategy = authStrategy;
    }

    public void executeStrategy(RegisterRequest request) throws Exception {
        authStrategy.register(request);
    }
}
