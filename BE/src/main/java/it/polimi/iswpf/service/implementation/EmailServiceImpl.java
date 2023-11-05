package it.polimi.iswpf.service.implementation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.polimi.iswpf.dto.request.InviaEmailRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.InternalServerErrorException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model._enum.EventType;
import it.polimi.iswpf.model.entity.User;
import it.polimi.iswpf.model.repository.UserRepository;
import it.polimi.iswpf.service._interface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
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
    private final Configuration config;

    //Caratteri con cui è possibile costruire la password.
    private static final String CARATTERI_VALIDI = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final Integer LUNGHEZZA_PASSWORD = 20;

    /**
     * Metodo che si occupa semplicemente dell'invio di una mail.
     * @param request DTO con destinatario, oggetto e testo della mail.
     */
    @Override
    public void inviaEmail(InviaEmailRequest request) {

        //Messaggio che rappresenta un'email MIME.
        MimeMessage message = emailSender.createMimeMessage();

        try {
            //Istanzio l'oggetto che semplifica la creazione e la configurazione delle email.
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, //Messaggio MIME.
                    //L'email può contenere multipart (es. ASCII speciali o allegati).
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    //Imposto la codifica UTF-8 per il contenuto dell'email.
                    StandardCharsets.UTF_8.name());

            //Scelta del template da utilizzare in base al tipo di email.
            Template template = config.getTemplate(
                    request.getEventType().equals(EventType.FOLLOWERS) ? "followersTemplate.ftl" :
                    request.getEventType().equals(EventType.NEWSLETTER) ? "newsletterTemplate.ftl" :
                    "recoveryPasswordTemplate.ftl");

            //Viene elaborato il template in base ai dati forniti (i campi dinamici della HashMap).
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, request.getDynamicData());

            helper.setTo(request.getEmailDestinatario()); //Setto il destinatario dell'email.
            helper.setSubject(request.getOggetto()); //Setto l'oggetto dell'mail.
            helper.setText(html, true); //Setto il corpo della mail e specifico che si tratta di un file HTML.

            //Invio l'email.
            emailSender.send(message);

        } catch(MessagingException | IOException | TemplateException e) {

            //Messaggio di errore nell'eventualità di eccezioni durante il processo di creazione o invio dell'email.
            throw new InternalServerErrorException("Errore durante l'invio della mail");
        }
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

        //HashMap dove salvare i dati dinamici da inserire nel template per personalizzare le mail.
        Map<String, String> dynamicData = new HashMap<>();

        //Aggiungo i dati e stabilisco una chiave tramite la quale accedo all'interno del template.
        dynamicData.put("nomeTurista", user.getNome());
        dynamicData.put("nuovaPassword", password.toString());

        //Invio la mail specificando, tramite il DTO, destinatario, oggetto e testo.
        inviaEmail(new InviaEmailRequest(
                email,
                "Cambio password EventGURU",
                dynamicData,
                EventType.RECOVERY_PASSWORD));
    }
}
