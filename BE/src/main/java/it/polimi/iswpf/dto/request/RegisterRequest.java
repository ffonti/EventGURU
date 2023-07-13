package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest {

    private String nome;

    private String cognome;

    private String email;

    private String username;

    private String password;
}
