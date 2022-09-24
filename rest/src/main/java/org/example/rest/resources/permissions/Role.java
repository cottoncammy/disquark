package org.example.rest.resources.permissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.immutables.value.Value.Enclosing;

import java.util.Optional;

@Enclosing
@ImmutableJson
public interface Role {

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

    int position();

    String permissions();

    boolean managed();

    boolean mentionable();

    Optional<Tags> tags();

    @ImmutableJson
    interface Tags {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("bot_id")
        Optional<Snowflake> botId();

        @JsonProperty("integration_id")
        Optional<Snowflake> integrationId();

        @JsonProperty("premium_subscriber")
        Optional<Void> premiumSubscriber();

        class Builder extends ImmutableRole.Tags.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableRole.Builder {
        protected Builder() {}
    }
}
