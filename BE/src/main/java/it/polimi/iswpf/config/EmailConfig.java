package it.polimi.iswpf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.Properties;

/**
 * Bean per la configurazione del servizio di mailing. Grazie all'annotazione "@Configuration",
 * il contenuto di questa classe viene eseguito appena il server viene avviato.
 */
@Configuration
public class EmailConfig {

    /**
     * Bean che ritorna un'istanza di {@link JavaMailSender} configurata in base
     * ai protocolli e alle credenziali adatte all'applicazione.
     * @return Istanza di {@link JavaMailSender}.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        //Configurazione dell'host, in questo caso gmail.
        mailSender.setHost("smtp.gmail.com");
        //Configurazione della porta che utilizzerà il protocollo SMTP.
        mailSender.setPort(587);

        //Account creato con il solo scopo di dimostrare il funzionamento di questo servizio.
        mailSender.setUsername("eventguru.service@gmail.com");
        //Password generata appositamente per l'uso in sviluppo.
        mailSender.setPassword("ktpz weaz pwjh iuxv");

        Properties props = mailSender.getJavaMailProperties();

        //Configurazione del protocollo di trasporto.
        props.put("mail.transport.protocol", "smtp");
        //Viene abilitata l'autenticazione SMTP.
        props.put("mail.smtp.auth", "true");
        /* Abilita il supporto STARTTLS, un metodo per proteggere la comunicazione tra
        client e server tramite crittografia durante il trasferimento dei dati. */
        props.put("mail.smtp.starttls.enable", "true");
        //Consente di tracciare, tramite il terminale, le varie operazioni eseguite durante l'uso del servizio.
        props.put("mail.debug", "true");

        return mailSender;
    }

    /**
     * Bean di configurazione per FreeMarker, un motore di template usato per ottimizzare il servizio di mailing.
     * @return Un'istanza del bean dopo aver configurato la directory dei templates.
     */
    @Bean
    @Primary //Utilizzata per risolvere delle ambiguità sulla scelta tra bean dello stesso tipo.
    public FreeMarkerConfigurationFactoryBean factoryBean() {

        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();

        //Seleziono la directory da cui prendere i templates.
        bean.setTemplateLoaderPath("classpath:/templates");

        return bean;
    }
}
