package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.sticker.CreateGuildSticker;
import org.example.rest.resources.sticker.ModifyGuildSticker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class StickerIT {
    private Snowflake stickerId;

    @Test
    @Order(1)
    void testGetSticker(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_STICKER_ID") Snowflake stickerId) {
        botClient.getSticker(stickerId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(2)
    void testListNitroStickerPacks(DiscordBotClient<?> botClient) {
        botClient.listNitroStickerPacks().subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(3)
    void testListGuildStickers(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.listGuildStickers(guildId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(4)
    void testCreateGuildSticker(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        CreateGuildSticker createGuildSticker = CreateGuildSticker.builder()
                .guildId(guildId)
                .name("foo")
                .description("bar")
                .tags("baz")
                .files(List.of())
                .build();

        stickerId = botClient.createGuildSticker(createGuildSticker)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(5)
    void testGetGuildSticker(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildSticker(guildId, stickerId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(6)
    void testModifyGuildSticker(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        ModifyGuildSticker modifyGuildSticker = ModifyGuildSticker.builder()
                .guildId(guildId)
                .stickerId(stickerId)
                .name("alice")
                .build();

        botClient.modifyGuildSticker(modifyGuildSticker)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(7)
    void testDeleteGuildSticker(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteGuildSticker(guildId, stickerId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}