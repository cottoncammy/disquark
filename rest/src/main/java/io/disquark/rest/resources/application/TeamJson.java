package io.disquark.rest.resources.application;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Team.class)
interface TeamJson {

    Optional<String> icon();

    Snowflake id();

    List<Team.Member> members();

    String name();

    @JsonProperty("owner_user_id")
    Snowflake ownerUserId();

    @ImmutableJson
    @JsonDeserialize(as = Team.Member.class)
    interface MemberJson {

        @JsonProperty("membership_state")
        MembershipState membershipState();

        List<String> permissions();

        @JsonProperty("team_id")
        Snowflake teamId();

        User user();
    }

    enum MembershipState {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        INVITED(1),
        ACCEPTED(2);

        private final int value;

        MembershipState(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }
}
