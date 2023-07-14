package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    private String username;

    private String password;
}
