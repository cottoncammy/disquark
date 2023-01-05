package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.scheduledevent.CreateGuildScheduledEvent;
import org.example.rest.resources.guild.scheduledevent.GetGuildScheduledEventUsers;
import org.example.rest.resources.guild.scheduledevent.GuildScheduledEvent;
import org.example.rest.resources.guild.scheduledevent.ModifyGuildScheduledEvent;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;

@ExtendWith(SomeExtension2.class)
class GuildScheduledEventIT {
    private Snowflake guildScheduledEventId;

    @Test
    @Order(1)
    void testListScheduledEventsForGuild(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.listScheduledEventsForGuild(guildId, false)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(2)
    void testCreateGuildScheduledEvent(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        CreateGuildScheduledEvent createGuildScheduledEvent = CreateGuildScheduledEvent.builder()
                .guildId(guildId)
                .name("foo")
                .privacyLevel(GuildScheduledEvent.PrivacyLevel.GUILD_ONLY)
                .scheduledStartTime(Instant.now())
                .entityType(GuildScheduledEvent.EntityType.VOICE)
                .build();

        guildScheduledEventId = botClient.createGuildScheduledEvent(createGuildScheduledEvent)
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
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetGuildScheduledEventUsers(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildScheduledEventUsers(GetGuildScheduledEventUsers.create(guildId, guildScheduledEventId))
                .subscribe().withSubscriber(AssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testModifyGuildScheduledEvent(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        ModifyGuildScheduledEvent modifyGuildScheduledEvent = ModifyGuildScheduledEvent.builder()
                .guildId(guildId)
                .guildScheduledEventId(guildScheduledEventId)
                .name("bar")
                .build();

        botClient.modifyGuildScheduledEvent(modifyGuildScheduledEvent)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(6)
    void testDeleteGuildScheduledEvent(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteGuildScheduledEvent(guildId, guildScheduledEventId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}