package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.emoji.CreateGuildEmoji;
import org.example.rest.resources.emoji.ModifyGuildEmoji;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class EmojiIT {
    private Snowflake emojiId;

    @Test
    @Order(1)
    void testListGuildEmojis(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.listGuildEmojis(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(2)
    void testCreateGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        Buffer image = botClient.getVertx().fileSystem().readFileAndAwait("images/emoji.png");
        CreateGuildEmoji createGuildEmoji = CreateGuildEmoji.builder()
                .guildId(guildId)
                .name("foo")
                .image(image)
                .build();

        emojiId = botClient.createGuildEmoji(createGuildEmoji)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id()
                .get();
    }

    @Test
    @Order(3)
    void testGetGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildEmoji(guildId, emojiId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testModifyGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.modifyGuildEmoji(ModifyGuildEmoji.builder().guildId(guildId).emojiId(emojiId).name("bar").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testDeleteGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteGuildEmoji(guildId, emojiId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
