package io.disquark.rest.json.guild;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.application.Application;
import io.disquark.rest.json.oauth2.Scope;
import io.disquark.rest.json.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Integration.class)
interface IntegrationJson {

    Snowflake id();

    String name();

    String type();

    Optional<Boolean> enabled();

    Optional<Boolean> syncing();

    @JsonProperty("role_id")
    Optional<Snowflake> roleId();

    @JsonProperty("enable_emoticons")
    Optional<Boolean> enableEmoticons();

    @JsonProperty("expire_behavior")
    Optional<Integration.ExpireBehavior> expireBehavior();

    @JsonProperty("expire_grace_period")
    OptionalInt expireGracePeriod();

    Optional<User> user();

    Integration.Account account();

    @JsonProperty("synced_at")
    Optional<Instant> syncedAt();

    @JsonProperty("subscriber_count")
    OptionalInt subscriberCount();

    Optional<Boolean> revoked();

    Optional<Integration.Application> application();

    Optional<List<Scope>> scopes();

    enum ExpireBehavior {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        REMOVE_ROLE(0),
        KICK(1);

        private final int value;

        ExpireBehavior(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = Integration.Account.class)
    interface AccountJson {

        String id();

        String name();
    }

    @ImmutableJson
    @JsonDeserialize(as = Integration.Application.class)
    interface ApplicationJson {

        Snowflake id();

        String name();

        Optional<String> icon();

        String description();

        Optional<User> bot();
    }
}
