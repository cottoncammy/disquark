package io.disquark.it;

import io.disquark.rest.DiscordBotClient;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DisQuarkParameterResolver.class)
class ApplicationIT {

    @Test
    void testGetCurrentBotApplicationInformation(DiscordBotClient<?> botClient) {
        botClient.getCurrentBotApplicationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
