package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.guild.Guild;
import org.immutables.value.Value.Enclosing;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableInvite.class)
public interface Invite {

    static Builder builder() {
        return new Builder();
    }

    String code();

    Optional<Guild> guild();

    Optional<Channel> channel();

    Optional<User> inviter();

    @JsonProperty("target_type")
    Optional<TargetType> targetType();

    @JsonProperty("target_user")
    Optional<User> targetUser();

    @JsonProperty("target_application")
    Optional<Application> targetApplication();

    @JsonProperty("approximate_presence_count")
    OptionalInt approximatePresenceCount();

    @JsonProperty("approximate_member_count")
    OptionalInt approximateMemberCount();

    @JsonProperty("expires_at")
    Optional<String> expiresAt();

    @Deprecated
    @JsonProperty("stage_instance")
    StageInstance stageInstance();

    @JsonProperty("guild_scheduled_event")
    GuildScheduledEvent guildScheduledEvent();

    enum TargetType {
        STREAM(1),
        EMBEDDED_APPLICATION(2);

        private final int value;

        TargetType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
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

    @Deprecated
    @ImmutableJson
    @JsonDeserialize(as = ImmutableInvite.StageInstance.class)
    interface StageInstance {

        static Builder builder() {
            return new Builder();
        }

        List<Guild.Member> members();

        @JsonProperty("participant_count")
        int participantCount();

        @JsonProperty("speaker_count")
        int speakerCount();

        String topic();

        class Builder extends ImmutableInvite.StageInstance.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableInvite.Builder {
        protected Builder() {}
    }
}
