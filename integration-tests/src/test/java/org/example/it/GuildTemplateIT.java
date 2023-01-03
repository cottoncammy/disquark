package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.extension.ConfigValue;
import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.template.CreateGuildFromGuildTemplate;
import org.example.rest.resources.guild.template.CreateGuildTemplate;
import org.example.rest.resources.guild.template.ModifyGuildTemplate;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SomeExtension2.class)
class GuildTemplateIT {
    private String templateCode;

    @Test
    @Order(1)
    void testCreateGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        CreateGuildTemplate createGuildTemplate = CreateGuildTemplate.builder()
                .guildId(guildId)
                .name("foo")
                .build();

        templateCode = botClient.createGuildTemplate(createGuildTemplate)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted()
                .getItem()
                .code();
    }

    @Test
    @Order(2)
    void testGetGuildTemplate(DiscordBotClient<?> botClient) {
        botClient.getGuildTemplate(templateCode).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(3)
    void testCreateGuildFromGuildTemplate(DiscordBotClient<?> botClient) {
        CreateGuildFromGuildTemplate createGuildFromGuildTemplate = CreateGuildFromGuildTemplate.builder()
                .name("foo")
                .build();

        botClient.createGuildFromGuildTemplate(createGuildFromGuildTemplate)
                .flatMap(guild -> botClient.deleteGuild(guild.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetGuildTemplates(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildTemplates(guildId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(5)
    void testSyncGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.syncGuildTemplate(guildId, templateCode).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(6)
    void testModifyGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        ModifyGuildTemplate modifyGuildTemplate = ModifyGuildTemplate.builder()
                .guildId(guildId)
                .templateCode(templateCode)
                .name("bar")
                .build();

        botClient.modifyGuildTemplate(modifyGuildTemplate).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(7)
    void testDeleteGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteGuildTemplate(guildId, templateCode).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }
}
