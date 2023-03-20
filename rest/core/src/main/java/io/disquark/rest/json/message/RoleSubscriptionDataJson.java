package io.disquark.rest.json.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = RoleSubscriptionData.class)
interface RoleSubscriptionDataJson {

    @JsonProperty("role_subscription_listing_id")
    Snowflake roleSubscriptionListingId();

    @JsonProperty("tier_name")
    String tierName();

    @JsonProperty("total_months_subscribed")
    int totalMonthsSubscribed();

    @JsonProperty("is_renewal")
    boolean isRenewal();
}
