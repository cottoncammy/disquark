package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.example.rest.DiscordBotClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SomeExtension2.class)
class ApplicationIT {

    @Test
    void testGetCurrentBotApplicationInformation(DiscordBotClient<?> botClient) {
        botClient.getCurrentBotApplicationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
