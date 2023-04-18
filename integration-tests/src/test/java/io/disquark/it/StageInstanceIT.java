package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.json.Snowflake;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("stage-channel")
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class StageInstanceIT {

    @Test
    @Order(1)
    void testCreateStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.createStageInstance(channelId, "foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(2)
    void testGetStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.getStageInstance(channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(3)
    void testModifyStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.modifyStageInstance(channelId)
                .withTopic("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testDeleteStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.deleteStageInstance(channelId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
