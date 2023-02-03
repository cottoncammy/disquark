package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.automod.AutoModerationAction;
import io.disquark.rest.resources.automod.AutoModerationRule;
import io.disquark.rest.resources.automod.CreateAutoModerationRule;
import io.disquark.rest.resources.automod.ModifyAutoModerationRule;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class AutoModerationIT {
    private Snowflake autoModerationRuleId;

    @Test
    @Order(1)
    void testListAutoModerationRulesForGuild(DiscordBotClient<?> botClient,
            @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.listAutoModerationRulesForGuild(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(2)
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
    @Order(3)
    void testGetAutoModerationRule(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getAutoModerationRule(guildId, autoModerationRuleId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
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
