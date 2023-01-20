package org.example.rest.resources.permissions;

import java.util.EnumSet;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableRole.class)
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

    EnumSet<PermissionFlag> permissions();

    boolean managed();

    boolean mentionable();

    Optional<Tags> tags();

    @ImmutableJson
    @JsonDeserialize(as = ImmutableRole.Tags.class)
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

        @JsonProperty("subscription_listing_id")
        Optional<Snowflake> subscriptionListingId();

        @JsonProperty("available_for_purchase")
        Optional<Void> availableForPurchase();

        @JsonProperty("guild_connections")
        Optional<Void> guildConnections();

        class Builder extends ImmutableRole.Tags.Builder {
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableRole.Builder {
        protected Builder() {
        }
    }
}
