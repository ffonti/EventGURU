package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.dto.request.InviaEmailRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service._interface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;
    private final UserRepository userRepository;

    private static final String CARATTERI_VALIDI = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final Integer LUNGHEZZA_PASSWORD = 20;

    @Override
    public void inviaEmail(String destinatario, InviaEmailRequest request) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("eventguru.service@gmail.com");
        message.setTo(destinatario);
        message.setSubject(request.getOggetto());
        message.setText(request.getTesto());

        emailSender.send(message);
    }

    @Override
    public void recuperaPassword(String email) {

        SimpleMailMessage message = new SimpleMailMessage();
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        if(email.isEmpty() || email.isBlank()) {
            throw new BadRequestException("Email non valida");
        }

        Optional<User> userExists = userRepository.findByEmail(email);

        if(userExists.isEmpty()) {
            throw new NotFoundException("L'email non è associata ad alcun utente");
        }

        for (int i = 0; i < LUNGHEZZA_PASSWORD; i++) {
            int index = random.nextInt(CARATTERI_VALIDI.length());
            password.append(CARATTERI_VALIDI.charAt(index));
        }

        User user = userExists.get();

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        message.setFrom("eventguru.service@gmail.com");
        message.setTo(email);
        message.setSubject("Nuova password EventGURU");
        message.setText("Ciao!\nÈ stata appena richiesta una nuova password.\nPuoi accedere di nuovo copiando la seguente password: "
                + password + "\n\nCi vediamo sulla nostra piattaforma!\nIl team EventGURU");

        emailSender.send(message);
    }
}
