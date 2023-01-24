package io.disquark.rest.resources.guild.template;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Locale;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.Channel;
import io.disquark.rest.resources.guild.Guild;
import io.disquark.rest.resources.permissions.PermissionFlag;
import io.disquark.rest.resources.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableGuildTemplate.class)
public interface GuildTemplate {

    static Builder builder() {
        return new Builder();
    }

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
    PartialGuild serializedSourceGuild();

    @JsonProperty("is_dirty")
    Optional<Boolean> isDirty();

    class Builder extends ImmutableGuildTemplate.Builder {
        protected Builder() {
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuildTemplate.PartialGuild.class)
    interface PartialGuild {

        static Builder builder() {
            return new Builder();
        }

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

        List<PartialRole> roles();

        List<Channel> channels();

        @JsonProperty("system_channel_id")
        Optional<Snowflake> systemChannelId();

        @JsonProperty("system_channel_flags")
        EnumSet<Guild.SystemChannelFlag> systemChannelFlags();

        Optional<String> description();

        @JsonProperty("preferred_locale")
        Locale preferredLocale();

        @ImmutableJson
        @JsonDeserialize(as = ImmutableGuildTemplate.PartialRole.class)
        interface PartialRole {

            static Builder builder() {
                return new Builder();
            }

            Snowflake id();

            String name();

            int color();

            boolean hoist();

            Optional<String> icon();

            @JsonProperty("unicode_emoji")
            Optional<String> unicodeEmoji();

            EnumSet<PermissionFlag> permissions();

            boolean mentionable();

            class Builder extends ImmutableGuildTemplate.PartialRole.Builder {
                protected Builder() {
                }
            }
        }

        class Builder extends ImmutableGuildTemplate.PartialGuild.Builder {
            protected Builder() {
            }
        }
    }
}
