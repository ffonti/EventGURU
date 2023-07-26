package it.polimi.iswpf.dto.response;

import it.polimi.iswpf.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private User user;

    private String message;

    private String jwt;
}
