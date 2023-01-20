package org.example.rest.resources.application;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.user.User;
import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableTeam.class)
public interface Team {

    static Builder builder() {
        return new Builder();
    }

    Optional<String> icon();

    Snowflake id();

    List<Member> members();

    String name();

    @JsonProperty("owner_user_id")
    Snowflake ownerUserId();

    @ImmutableJson
    @JsonDeserialize(as = ImmutableTeam.Member.class)
    interface Member {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("membership_state")
        MembershipState membershipState();

        List<String> permissions();

        @JsonProperty("team_id")
        Snowflake teamId();

        User user();

        class Builder extends ImmutableTeam.Member.Builder {
            protected Builder() {}
        }
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

    class Builder extends ImmutableTeam.Builder {
        protected Builder() {}
    }
}
