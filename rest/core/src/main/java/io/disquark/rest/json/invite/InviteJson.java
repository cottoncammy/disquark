package io.disquark.rest.json.invite;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.json.guild.Guild;
import io.disquark.rest.json.scheduledevent.GuildScheduledEvent;
import io.disquark.rest.json.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Invite.class)
interface InviteJson {

    String code();

    Optional<Invite.PartialGuild> guild();

    Optional<Channel> channel();

    Optional<User> inviter();

    @JsonProperty("target_type")
    Optional<TargetType> targetType();

    @JsonProperty("target_user")
    Optional<User> targetUser();

    @JsonProperty("target_application")
    Optional<Invite.EmbeddedApplication> targetApplication();

    @JsonProperty("approximate_presence_count")
    OptionalInt approximatePresenceCount();

    @JsonProperty("approximate_member_count")
    OptionalInt approximateMemberCount();

    @JsonProperty("expires_at")
    Optional<String> expiresAt();

    @JsonProperty("guild_scheduled_event")
    Optional<GuildScheduledEvent> guildScheduledEvent();

    @JsonUnwrapped
    Optional<MetadataJson> metadata();

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
    @JsonDeserialize(as = Invite.PartialGuild.class)
    interface PartialGuildJson {

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
    }

    @ImmutableJson
    @JsonDeserialize(as = Invite.Metadata.class)
    interface MetadataJson {

        int uses();

        @JsonProperty("max_uses")
        int maxUses();

        @JsonProperty("max_age")
        int maxAge();

        boolean temporary();

        @JsonProperty("created_at")
        Instant createdAt();
    }

    @ImmutableJson
    @JsonDeserialize(as = Invite.EmbeddedApplication.class)
    interface EmbeddedApplicationJson {

        String name();

        Optional<String> icon();

        String description();

        @JsonProperty("max_participants")
        int maxParticipants();
    }
}
