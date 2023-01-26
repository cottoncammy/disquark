package io.disquark.it;

import java.time.Duration;
import java.time.Instant;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.Channel;
import io.disquark.rest.resources.guild.scheduledevent.CreateGuildScheduledEvent;
import io.disquark.rest.resources.guild.scheduledevent.GetGuildScheduledEventUsers;
import io.disquark.rest.resources.guild.scheduledevent.GuildScheduledEvent;
import io.disquark.rest.resources.guild.scheduledevent.ModifyGuildScheduledEvent;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DisQuarkParameterResolver.class)
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

        CreateGuildScheduledEvent createGuildScheduledEvent = CreateGuildScheduledEvent.builder()
                .guildId(guildId)
                .channelId(voiceChannelId)
                .name("foo")
                .privacyLevel(GuildScheduledEvent.PrivacyLevel.GUILD_ONLY)
                .scheduledStartTime(Instant.now().plus(Duration.ofMinutes(30)))
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
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetGuildScheduledEventUsers(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildScheduledEventUsers(GetGuildScheduledEventUsers.create(guildId, guildScheduledEventId))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
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