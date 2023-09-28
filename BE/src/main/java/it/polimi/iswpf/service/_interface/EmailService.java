package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.InviaEmailRequest;
import it.polimi.iswpf.service.implementation.EmailServiceImpl;

/**
 * Interfaccia che contiene le firme dei metodi del service che gestisce il mailing.
 * Implementazione -> {@link EmailServiceImpl}.
 */
public interface EmailService {

    /**
     * Metodo che si occupa semplicemente dell'invio di una mail.
     * @param request DTO con destinatario, oggetto e testo della mail.
     */
    void inviaEmail(InviaEmailRequest request);

    /**
     * Metodo che cambia la password dell'utente e invia quella nuova tramite mail.
     * @param email Email a cui inviare la nuova password.
     */
    void recuperaPassword(String email);
}
