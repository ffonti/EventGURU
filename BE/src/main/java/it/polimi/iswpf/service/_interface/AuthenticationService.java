package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.LoginRequest;
import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.dto.response.LoginResponse;
import it.polimi.iswpf.service.implementation.AuthenticationServiceImpl;
import lombok.NonNull;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link AuthenticationServiceImpl}.
 */
public interface AuthenticationService {
    void register(@NonNull RegisterRequest request) throws Exception;

    LoginResponse login(@NonNull LoginRequest request);
}
