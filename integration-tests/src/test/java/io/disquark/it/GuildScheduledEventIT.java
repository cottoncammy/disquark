package io.disquark.it;

import java.time.Duration;
import java.time.Instant;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.json.scheduledevent.GuildScheduledEvent;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class GuildScheduledEventIT {
    private Snowflake guildScheduledEventId;

    @Test
    @Order(1)
    void testListScheduledEventsForGuild(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.listScheduledEventsForGuild(guildId, false)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(2)
    void testCreateGuildScheduledEvent(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        Snowflake voiceChannelId = botClient.getGuildChannels(guildId)
                .filter(channel -> channel.type() == Channel.Type.GUILD_VOICE)
                .toUni()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();

        guildScheduledEventId = botClient.createGuildScheduledEvent(guildId, "foo",
                GuildScheduledEvent.PrivacyLevel.GUILD_ONLY, Instant.now().plus(Duration.ofMinutes(30)),
                GuildScheduledEvent.EntityType.VOICE)
                .withChannelId(voiceChannelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(3)
    void testGetGuildScheduledEvent(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildScheduledEvent(guildId, guildScheduledEventId, false)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetGuildScheduledEventUsers(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildScheduledEventUsers(guildId, guildScheduledEventId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testModifyGuildScheduledEvent(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.modifyGuildScheduledEvent(guildId, guildScheduledEventId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(6)
    void testDeleteGuildScheduledEvent(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteGuildScheduledEvent(guildId, guildScheduledEventId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
