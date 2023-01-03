package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.extension.ConfigValue;
import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.emoji.CreateGuildEmoji;
import org.example.rest.resources.emoji.ModifyGuildEmoji;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class EmojiIT {
    private Snowflake emojiId;

    @Test
    @Order(1)
    void testListGuildEmojis(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.listGuildEmojis(guildId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(2)
    void testCreateGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        CreateGuildEmoji createGuildEmoji = CreateGuildEmoji.builder()
                .name("foo")
                .image("")
                .build();

        emojiId = botClient.createGuildEmoji(createGuildEmoji)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted()
                .getItem()
                .id()
                .get();
    }

    @Test
    @Order(3)
    void testGetGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildEmoji(guildId, emojiId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(4)
    void testModifyGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        ModifyGuildEmoji modifyGuildEmoji = ModifyGuildEmoji.builder()
                .guildId(guildId)
                .emojiId(emojiId)
                .name("bar")
                .build();

        botClient.modifyGuildEmoji(modifyGuildEmoji).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(5)
    void testDeleteGuildEmoji(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteGuildEmoji(guildId, emojiId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}
