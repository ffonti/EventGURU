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

/**
 * Service per gestire tutti i metodi inerenti al mailing.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final PasswordEncoder passwordEncoder; //Bean per codificare la password.
    private final JavaMailSender emailSender; //Bean che offre un metodo per inviare le mail.
    private final UserRepository userRepository;

    //Caratteri con cui è possibile cosruire la password.
    private static final String CARATTERI_VALIDI = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final Integer LUNGHEZZA_PASSWORD = 20;

    /**
     * Metodo che si occupa semplicemente dell'invio di una mail.
     * @param request DTO con destinatario, oggetto e testo della mail.
     */
    @Override
    public void inviaEmail(InviaEmailRequest request) {

        //Controllo che tutti i campi siano compilati.
        if(request.getEmailDestinatario().isBlank() || request.getEmailDestinatario().isEmpty() ||
            request.getOggetto().isBlank() || request.getOggetto().isEmpty() ||
            request.getTesto().isBlank() || request.getTesto().isEmpty()) {
            throw new BadRequestException("Dati della mail mancanti");
        }

        //Istanza l'oggetto che verrà inviato tramite mail.
        SimpleMailMessage message = new SimpleMailMessage();

        //Salvo i dati della mail.
        message.setFrom("eventguru.service@gmail.com");
        message.setTo(request.getEmailDestinatario());
        message.setSubject(request.getOggetto());
        message.setText(request.getTesto());

        //Invio la mail tramite JavaMailSender.
        emailSender.send(message);
    }

    /**
     * Metodo che cambia la password dell'utente e invia quella nuova tramite mail.
     * @param email Email a cui inviare la nuova password.
     */
    @Override
    public void recuperaPassword(String email) {

        //Istanza di un generatore di numeri random.
        SecureRandom random = new SecureRandom();
        //Istanza di StringBuilder tramite il quale verrà costruita la nuova password.
        StringBuilder password = new StringBuilder();

        //Controllo la validità della mail.
        if(email.isEmpty() || email.isBlank()) {
            throw new BadRequestException("Email non valida");
        }

        //Controllo se esiste un utente con quella email.
        Optional<User> userExists = userRepository.findFirstByEmail(email);

        //Se l'utente non esiste, lancio un'eccezione.
        if(userExists.isEmpty()) {
            throw new NotFoundException("L'email non è associata ad alcun utente");
        }

        //Creo una password lunga 20 caratteri.
        for (int i = 0; i < LUNGHEZZA_PASSWORD; i++) {
            //Prendo un intero random che rappresenta un carattere presente in CARATTERI_VALIDI.
            int index = random.nextInt(CARATTERI_VALIDI.length());
            //Aggiungo alla password il carattere presente in posizione dell'intero.
            password.append(CARATTERI_VALIDI.charAt(index));
        }

        User user = userExists.get();

        //Setto la nuova password all'utente.
        user.setPassword(passwordEncoder.encode(password));

        //Salvo le modifiche sul database.
        userRepository.save(user);

        //Invio la mail specificando, tramite il DTO, destinatario, oggetto e testo.
        inviaEmail(new InviaEmailRequest(
                email,
                "Nuova password EventGURU",
                "Ciao " + user.getNome() + "!\nÈ stata appena richiesta una nuova password." +
                "\nAdesso puoi accedere con la seguente password: "
                + password + "\n\nCi vediamo sulla nostra piattaforma!\nIl team di EventGURU"));
    }
}
