package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.guild.template.CreateGuildFromGuildTemplate;
import io.disquark.rest.resources.guild.template.CreateGuildTemplate;
import io.disquark.rest.resources.guild.template.ModifyGuildTemplate;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class GuildTemplateIT {
    private String templateCode;

    @BeforeAll
    void init(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildTemplates(guildId)
                .onItem().transformToUniAndMerge(template -> botClient.deleteGuildTemplate(guildId, template.code()))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem();
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
