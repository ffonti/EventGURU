package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserDataRequest {

    private String nome;

    private String cognome;

    private String email;

    private String username;

    private String vecchiaPassword;

    private String nuovaPassword;

    private boolean iscrittoNewsletter;

    private String oldUsername;
}
