package it.polimi.iswpf.dto.response;

import it.polimi.iswpf.model._enum.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long userId;

    private String nome;

    private String cognome;

    private String email;

    private String username;

    private String password;

    private Ruolo ruolo;

    private boolean iscrittoNewsletter;
}
