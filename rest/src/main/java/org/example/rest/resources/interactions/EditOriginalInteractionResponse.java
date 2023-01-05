package org.example.rest.resources.interactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.MultipartRequest;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.AllowedMentions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.components.Component;

import java.util.List;
import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
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

    Optional<List<Message.Attachment>> attachments();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/webhooks/{application.id}/{interaction.token}/messages/@original", false))
                .variables(variables("application.id", applicationId().getValue(), "interaction.token", interactionToken()))
                .body(this)
                .files(files())
                .build();
    }

    class Builder extends ImmutableEditOriginalInteractionResponse.Builder {
        protected Builder() {}
    }
}