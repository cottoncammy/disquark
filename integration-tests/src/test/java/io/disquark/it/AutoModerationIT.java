package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.automod.AutoModerationAction;
import io.disquark.rest.json.automod.AutoModerationRule;
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
        autoModerationRuleId = botClient.createAutoModerationRule(guildId, "foo",
                        AutoModerationRule.EventType.MESSAGE_SEND, AutoModerationRule.TriggerType.SPAM)
                .withActions(new AutoModerationAction(AutoModerationAction.Type.BLOCK_MESSAGE))
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
        botClient.modifyAutoModerationRule(guildId, autoModerationRuleId)
                .withName("bar")
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
