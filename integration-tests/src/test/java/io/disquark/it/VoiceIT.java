package io.disquark.it;

import io.disquark.rest.DiscordBotClient;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DisQuarkParameterResolver.class)
class VoiceIT {

    @Test
    void testListVoiceRegions(DiscordBotClient<?> botClient) {
        botClient.listVoiceRegions()
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
