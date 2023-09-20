package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.InviaEmailRequest;
import it.polimi.iswpf.service._interface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void inviaEmail(String destinatario, InviaEmailRequest request) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("eventguru.service@gmail.com");
        message.setTo(destinatario);
        message.setSubject(request.getOggetto());
        message.setText(request.getTesto());

        emailSender.send(message);
    }
}
