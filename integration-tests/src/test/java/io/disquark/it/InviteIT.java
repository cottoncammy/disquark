package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.json.Snowflake;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class InviteIT {
    private String inviteCode;

    @Test
    @Order(1)
    void testCreateInvite(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        inviteCode = botClient.createChannelInvite(channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .code();
    }

    @Test
    @Order(2)
    void testGetInvite(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        botClient.getInvite(inviteCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(3)
    void testDeleteInvite(DiscordBotClient<?> botClient) {
        botClient.deleteInvite(inviteCode, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
