package it.polimi.iswpf.unit.observer.configuration;

import it.polimi.iswpf.observer.configuration.ObserverConfig;
import it.polimi.iswpf.observer.listener.FollowersListener;
import it.polimi.iswpf.observer.listener.NewsletterListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class ObserverConfigTest {

    @Mock
    FollowersListener followersListener;

    @Mock
    NewsletterListener newsletterListener;

    @InjectMocks
    ObserverConfig observerConfig;

    @Test
    void initObserver() {

        assertAll(() -> observerConfig.initObserver());
    }

    @Test
    void onDestroy() {

        assertAll(() -> observerConfig.onDestroy());
    }
}