package it.polimi.iswpf.observer.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
public class FollowersListener implements EventListener {

    private final JavaMailSender emailSender;

    @Override
    public void update() {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("eventguru.service@gmail.com");
        message.setTo("fabriziofontana02@gmail.com");
        message.setSubject("Observer");
        message.setText("L'observer è riuscito");

        emailSender.send(message);
    }
}
