package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.automod.AutoModerationAction;
import org.example.rest.resources.automod.AutoModerationRule;
import org.example.rest.resources.automod.CreateAutoModerationRule;
import org.example.rest.resources.automod.ModifyAutoModerationRule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class AutoModerationIT {
    private Snowflake autoModerationRuleId;

    @Test
    @Order(1)
    void testListAutoModerationRulesForGuild(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.listAutoModerationRulesForGuild(guildId)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitCompletion()
                .assertCompleted();
    }

    @Test
    @Order(2)
    void testGetAutoModerationRule(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getAutoModerationRule(guildId, autoModerationRuleId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(3)
    void testCreateAutoModerationRule(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        CreateAutoModerationRule createAutoModerationRule = CreateAutoModerationRule.builder()
                .guildId(guildId)
                .name("foo")
                .eventType(AutoModerationRule.EventType.MESSAGE_SEND)
                .triggerType(AutoModerationRule.TriggerType.SPAM)
                .addAction(AutoModerationAction.builder().type(AutoModerationAction.Type.BLOCK_MESSAGE).build())
                .build();

        autoModerationRuleId = botClient.createAutoModerationRule(createAutoModerationRule)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(4)
    void testModifyAutoModerationRule(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        ModifyAutoModerationRule modifyAutoModerationRule = ModifyAutoModerationRule.builder()
                .guildId(guildId)
                .autoModerationRuleId(autoModerationRuleId)
                .name("bar")
                .build();

        botClient.modifyAutoModerationRule(modifyAutoModerationRule)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testDeleteAutoModerationRule(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.deleteAutoModerationRule(guildId, autoModerationRuleId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
