package it.polimi.iswpf.unit.observer.publisher;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model._enum.EventType;
import it.polimi.iswpf.model.entity.Evento;
import it.polimi.iswpf.model.entity.User;
import it.polimi.iswpf.observer.listener.EventListener;
import it.polimi.iswpf.observer.listener.NewsletterListener;
import it.polimi.iswpf.observer.publisher.EventManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventManagerTest {

    @Mock
    Map<EventType, EventListener> listeners;

    @Mock
    NewsletterListener newsletterListener;

    @InjectMocks
    EventManager eventManager;

    @Test
    void getInstance() {

        assertAll(EventManager::getInstance);
    }

    @Test
    void subscribe() {

        assertAll(() -> eventManager.subscribe(EventType.NEWSLETTER, newsletterListener));
    }

    @Test
    void unsubscribe() {

        assertAll(() -> eventManager.unsubscribe(EventType.NEWSLETTER));
    }

    @Test
    void testNotifyThrowsEventoNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventManager.notify(null, null, null, null));
    }

    @Test
    void testNotifyThrowsOrganizzatoreNonEsistente() {

        assertThrows(NotFoundException.class,
                () -> eventManager.notify(EventType.NEWSLETTER, null, null, null));
    }

    @Test
    void testNotifyThrowsTuristiNonEsistenti() {

        assertThrows(NotFoundException.class,
                () -> eventManager.notify(EventType.NEWSLETTER, new User(), null, null));
    }

    @Test
    void testNotifyThrowsEventoNonEsistente() {

        assertThrows(NotFoundException.class,
                () -> eventManager.notify(EventType.NEWSLETTER, new User(), List.of(new User()), null));
    }

    @Test
    void testNotifySuccessful() {

        User organizzatore = new UserBuilder()
                .nome("nome")
                .cognome("cognome")
                .build();

        List<User> turisti = new ArrayList<>();

        turisti.add(new UserBuilder()
                .email("fabriziofontana02@gmail.com")
                .build());

        Evento evento = new EventoBuilder()
                .titolo("titolo")
                .descrizione("descrizione")
                .dataInizio(LocalDateTime.now())
                .dataFine(LocalDateTime.now())
                .build();

        when(listeners.get(EventType.NEWSLETTER)).thenReturn(newsletterListener);

        assertAll(() -> eventManager.notify(EventType.NEWSLETTER, organizzatore, turisti, evento));
    }
}