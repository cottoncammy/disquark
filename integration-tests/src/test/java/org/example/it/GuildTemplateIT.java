package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.template.CreateGuildFromGuildTemplate;
import org.example.rest.resources.guild.template.CreateGuildTemplate;
import org.example.rest.resources.guild.template.ModifyGuildTemplate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class GuildTemplateIT {
    private String templateCode;

    @BeforeAll
    void init(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildTemplates(guildId)
                .onItem().transformToUniAndMerge(template -> botClient.deleteGuildTemplate(guildId, template.code()))
                .collect().asList()
                .await().indefinitely();
    }

    @Test
    @Order(1)
    void testCreateGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        templateCode = botClient.createGuildTemplate(CreateGuildTemplate.builder().guildId(guildId).name("foo").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .code();
    }

    @Test
    @Order(2)
    void testGetGuildTemplate(DiscordBotClient<?> botClient) {
        botClient.getGuildTemplate(templateCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(3)
    void testCreateGuildFromGuildTemplate(DiscordBotClient<?> botClient) {
        CreateGuildFromGuildTemplate createGuildFromGuildTemplate = CreateGuildFromGuildTemplate.builder()
                .templateCode(templateCode)
                .name("foo")
                .build();

        botClient.createGuildFromGuildTemplate(createGuildFromGuildTemplate)
                .flatMap(guild -> botClient.deleteGuild(guild.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetGuildTemplates(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildTemplates(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testSyncGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.syncGuildTemplate(guildId, templateCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(6)
    void testModifyGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        ModifyGuildTemplate modifyGuildTemplate = ModifyGuildTemplate.builder()
                .guildId(guildId)
                .templateCode(templateCode)
                .name("bar")
                .build();

        botClient.modifyGuildTemplate(modifyGuildTemplate)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(7)
    void testDeleteGuildTemplate(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteGuildTemplate(guildId, templateCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
