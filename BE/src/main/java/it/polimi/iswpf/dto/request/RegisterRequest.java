package it.polimi.iswpf.dto.request;

import it.polimi.iswpf.model.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    private String nome;

    private String cognome;

    private String email;

    private String username;

    private String password;

    private String ruolo;
}
