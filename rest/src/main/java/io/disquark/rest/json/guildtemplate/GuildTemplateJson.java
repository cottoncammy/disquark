package io.disquark.rest.json.guildtemplate;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Locale;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.json.guild.Guild;
import io.disquark.rest.json.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = GuildTemplate.class)
interface GuildTemplateJson {

    String code();

    String name();

    Optional<String> description();

    @JsonProperty("usage_count")
    int usageCount();

    @JsonProperty("creator_id")
    Snowflake creatorId();

    User creator();

    @JsonProperty("created_at")
    Instant createdAt();

    @JsonProperty("updated_at")
    Instant updatedAt();

    @JsonProperty("source_guild_id")
    Snowflake sourceGuildId();

    @JsonProperty("serialized_source_guild")
    GuildTemplate.PartialGuild serializedSourceGuild();

    @JsonProperty("is_dirty")
    Optional<Boolean> isDirty();

    @ImmutableJson
    @JsonDeserialize(as = GuildTemplate.PartialGuild.class)
    interface PartialGuildJson {

        String name();

        @JsonProperty("icon_hash")
        Optional<String> iconHash();

        @Deprecated
        Optional<String> region();

        @JsonProperty("afk_channel_id")
        Optional<Snowflake> afkChannelId();

        @JsonProperty("afk_timeout")
        int afkTimeout();

        @JsonProperty("verification_level")
        Guild.VerificationLevel verificationLevel();

        @JsonProperty("default_message_notifications")
        Guild.DefaultMessageNotificationLevel defaultMessageNotifications();

        @JsonProperty("explicit_content_filter")
        Guild.ExplicitContentFilterLevel explicitContentFilter();

        List<GuildTemplate.PartialRole> roles();

        List<Channel> channels();

        @JsonProperty("system_channel_id")
        Optional<Snowflake> systemChannelId();

        @JsonProperty("system_channel_flags")
        EnumSet<Guild.SystemChannelFlag> systemChannelFlags();

        Optional<String> description();

        @JsonProperty("preferred_locale")
        Locale preferredLocale();

        @ImmutableJson
        @JsonDeserialize(as = GuildTemplate.PartialRole.class)
        interface PartialRoleJson {

            Snowflake id();

            String name();

            int color();

            boolean hoist();

            Optional<String> icon();

            @JsonProperty("unicode_emoji")
            Optional<String> unicodeEmoji();

            EnumSet<PermissionFlag> permissions();

            boolean mentionable();
        }
    }
}
