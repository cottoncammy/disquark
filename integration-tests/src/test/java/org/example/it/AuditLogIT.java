package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.extension.ConfigValue;
import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.auditlog.GetGuildAuditLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SomeExtension2.class)
class AuditLogIT {

    @Test
    void testGetGuildAuditLog(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildAuditLog(GetGuildAuditLog.create(guildId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}
