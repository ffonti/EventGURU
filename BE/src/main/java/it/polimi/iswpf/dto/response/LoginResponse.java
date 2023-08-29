package it.polimi.iswpf.dto.response;

import it.polimi.iswpf.model.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long userId;

    private String nome;

    private String cognome;

    private String username;

    private Ruolo ruolo;

    private String email;

    private boolean iscrittoNewsletter;

    private String message;

    private String jwt;
}
