package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.extension.ConfigValue;
import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.stageinstance.CreateStageInstance;
import org.example.rest.resources.stageinstance.ModifyStageInstance;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class StageInstanceIT {

    @Test
    @Order(1)
    void testCreateStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.createStageInstance(CreateStageInstance.builder().channelId(channelId).topic("foo").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(2)
    void testGetStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.getStageInstance(channelId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(3)
    void testModifyStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.modifyStageInstance(ModifyStageInstance.builder().channelId(channelId).topic("bar").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testDeleteStageInstance(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.deleteStageInstance(channelId, null).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }
}
