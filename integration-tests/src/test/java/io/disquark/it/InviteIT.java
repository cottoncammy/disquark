package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.CreateChannelInvite;
import io.disquark.rest.resources.invite.GetInvite;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkParameterResolver.class)
class InviteIT {
    private String inviteCode;

    @Test
    @Order(1)
    void testGetInvite(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        inviteCode = botClient.createChannelInvite(CreateChannelInvite.create(channelId))
                .call(invite -> botClient.getInvite(GetInvite.create(invite.code())))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .code();
    }

    @Test
    @Order(2)
    void testDeleteInvite(DiscordBotClient<?> botClient) {
        botClient.deleteInvite(inviteCode, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}