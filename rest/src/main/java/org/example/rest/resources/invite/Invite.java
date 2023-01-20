package org.example.rest.resources.invite;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.guild.scheduledevent.GuildScheduledEvent;
import org.example.rest.resources.user.User;
import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableInvite.class)
public interface Invite {

    static Builder builder() {
        return new Builder();
    }

    String code();

    Optional<PartialGuild> guild();

    Optional<Channel> channel();

    Optional<User> inviter();

    @JsonProperty("target_type")
    Optional<TargetType> targetType();

    @JsonProperty("target_user")
    Optional<User> targetUser();

    @JsonProperty("target_application")
    Optional<EmbeddedApplication> targetApplication();

    @JsonProperty("approximate_presence_count")
    OptionalInt approximatePresenceCount();

    @JsonProperty("approximate_member_count")
    OptionalInt approximateMemberCount();

    @JsonProperty("expires_at")
    Optional<String> expiresAt();

    @JsonProperty("guild_scheduled_event")
    Optional<GuildScheduledEvent> guildScheduledEvent();

    enum TargetType {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        STREAM(1),
        EMBEDDED_APPLICATION(2);

        private final int value;

        TargetType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableInvite.PartialGuild.class)
    interface PartialGuild {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        String name();

        Optional<String> icon();

        Optional<String> splash();

        @JsonProperty("verification_level")
        Guild.VerificationLevel verificationLevel();

        List<Guild.Feature> features();

        @JsonProperty("vanity_url_code")
        Optional<String> vanityUrlCode();

        Optional<String> description();

        Optional<String> banner();

        @JsonProperty("premium_subscription_count")
        OptionalInt premiumSubscriptionCount();

        @JsonProperty("nsfw_level")
        Guild.NsfwLevel nsfwLevel();

        class Builder extends ImmutableInvite.PartialGuild.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableInvite.Metadata.class)
    interface Metadata {

        static Builder builder() {
            return new Builder();
        }

        int uses();

        @JsonProperty("max_uses")
        int maxUses();

        @JsonProperty("max_age")
        int maxAge();

        boolean temporary();

        @JsonProperty("created_at")
        Instant createdAt();

        class Builder extends ImmutableInvite.Metadata.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = Invite.EmbeddedApplication.class)
    interface EmbeddedApplication {

        static Builder builder () {
            return new Builder();
        }

        String name();

        Optional<String> icon();

        String description();

        @JsonProperty("max_participants")
        int maxParticipants();

        class Builder extends ImmutableInvite.EmbeddedApplication.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableInvite.Builder {
        protected Builder() {}
    }
}
