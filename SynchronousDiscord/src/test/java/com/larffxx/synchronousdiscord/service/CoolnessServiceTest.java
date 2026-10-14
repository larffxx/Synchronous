package com.larffxx.synchronousdiscord.service;

import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.infrastructure.repo.UsersConnectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoolnessServiceTest {
    private final UsersConnectRepository repository = mock(UsersConnectRepository.class);
    private final ModelCoolnessScorer scorer = mock(ModelCoolnessScorer.class);
    private final CoolnessService service = new CoolnessService(repository, scorer);

    @AfterEach
    void shutDownScoringPool() {
        service.shutdown();
    }

    @Test
    void returnsNullWhenTelegramUserIsNotRegistered() {
        when(repository.findByTelegramName("missing")).thenReturn(null);

        assertNull(service.getCoolnessByTelegramName("missing"));
    }

    @Test
    void returnsStoredTelegramUserCoolness() {
        UsersConnect user = new UsersConnect();
        user.setCoolness(42);
        when(repository.findByTelegramName("alice")).thenReturn(user);

        assertEquals(42, service.getCoolnessByTelegramName("alice"));
    }

    @Test
    void convertsBoundedCoolnessToStars() {
        assertEquals(0, service.starsFor(-100));
        assertEquals(0, service.starsFor(19));
        assertEquals(1, service.starsFor(20));
        assertEquals(5, service.starsFor(100));
        assertEquals(5, service.starsFor(120));
    }
}
