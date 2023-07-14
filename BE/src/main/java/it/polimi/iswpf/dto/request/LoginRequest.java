package it.polimi.iswpf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder //TODO implementare da zero il pattern
@AllArgsConstructor
public class LoginRequest {

    private String username;

    private String password;
}
