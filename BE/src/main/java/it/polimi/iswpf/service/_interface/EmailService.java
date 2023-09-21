package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.InviaEmailRequest;

public interface EmailService {

    void inviaEmail(InviaEmailRequest request);

    void recuperaPassword(String email);
}
