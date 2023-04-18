package io.disquark.rest.json.member;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.FlagEnum;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.user.User;

@ImmutableJson
@JsonDeserialize(as = GuildMember.class)
interface GuildMemberJson {

    Optional<User> user();

    Optional<String> nick();

    Optional<String> avatar();

    List<Snowflake> roles();

    @JsonProperty("joined_at")
    Instant joinedAt();

    @JsonProperty("premium_since")
    Optional<Instant> premiumSince();

    boolean deaf();

    boolean mute();

    EnumSet<GuildMember.Flag> flags();

    Optional<Boolean> pending();

    Optional<EnumSet<PermissionFlag>> permissions();

    @JsonProperty("communication_disabled_until")
    Optional<Instant> communicationDisabledUntil();

    enum Flag implements FlagEnum {
        DID_REJOIN(0),
        COMPLETED_ONBOARDING(1),
        BYPASSES_VERIFICATION(2),
        STARTED_ONBOARDING(3);

        private final int value;

        Flag(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }
}
