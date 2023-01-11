package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import org.example.rest.DiscordBotClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SomeExtension2.class)
class VoiceIT {

    @Test
    void testListVoiceRegions(DiscordBotClient<?> botClient) {
        botClient.listVoiceRegions()
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitCompletion()
                .assertCompleted();
    }
}
