package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.immutables.ImmutableJson;
import org.immutables.value.Value.Enclosing;

import java.util.List;
import java.util.Optional;

@Enclosing
@ImmutableJson
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
        INVITED(1),
        ACCEPTED(2);

        private final int value;

        MembershipState(int value) {
            this.value = value;
        }
    }

    class Builder extends ImmutableTeam.Builder {
        protected Builder() {}
    }
}
