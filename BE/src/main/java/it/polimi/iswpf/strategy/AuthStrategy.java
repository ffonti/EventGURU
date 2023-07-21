package it.polimi.iswpf.strategy;

import it.polimi.iswpf.dto.request.RegisterRequest;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import lombok.NonNull;

import java.util.List;

public interface AuthStrategy {

    Ruolo getRuolo();

    User register(RegisterRequest request) throws Exception;

    void checkUserData(List<String> dataList) throws Exception;
}
