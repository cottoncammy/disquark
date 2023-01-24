package io.disquark.rest.resources.interactions;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.AllowedMentions;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.components.Component;
import io.disquark.rest.resources.partial.PartialAttachment;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface EditOriginalInteractionResponse extends MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonIgnore
    String interactionToken();

    Optional<String> content();

    Optional<List<Message.Embed>> embeds();

    @JsonProperty("allowed_mentions")
    Optional<AllowedMentions> allowedMentions();

    Optional<List<Component>> components();

    Optional<List<PartialAttachment>> attachments();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/webhooks/{application.id}/{interaction.token}/messages/@original",
                        false))
                .variables(variables("application.id", applicationId().getValue(), "interaction.token", interactionToken()))
                .body(this)
                .files(files())
                .build();
    }

    class Builder extends ImmutableEditOriginalInteractionResponse.Builder {
        protected Builder() {
        }
    }
}
