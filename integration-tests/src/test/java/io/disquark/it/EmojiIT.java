package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.json.Snowflake;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.buffer.Buffer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
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
        emojiId = botClient.createGuildEmoji(guildId, "foo", image)
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
        botClient.modifyGuildEmoji(guildId, emojiId)
                .withName("bar")
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
