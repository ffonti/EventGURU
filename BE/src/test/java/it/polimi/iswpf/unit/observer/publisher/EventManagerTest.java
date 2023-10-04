package it.polimi.iswpf.unit.observer.publisher;

import it.polimi.iswpf.model.EventType;
import it.polimi.iswpf.observer.listener.NewsletterListener;
import it.polimi.iswpf.observer.publisher.EventManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class EventManagerTest {

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

//    @Test
//    void testNotify() {
//
//        User organizzatore = new UserBuilder()
//                .nome("nome")
//                .cognome("cognome")
//                .build();
//
//        List<User> turisti = new ArrayList<>();
//
//        turisti.add(new UserBuilder()
//                .email("fabriziofontana02@gmail.com")
//                .build());
//
//        Evento evento = new EventoBuilder()
//                .titolo("titolo")
//                .descrizione("descrizione")
//                .dataInizio(LocalDateTime.now())
//                .dataFine(LocalDateTime.now())
//                .build();
//
//        assertAll(() -> eventManager.notify(EventType.NEWSLETTER, organizzatore, turisti, evento));
//    }
}