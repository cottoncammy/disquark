package org.example.rest.resources.channel.forum;

import static org.example.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.MultipartRequest;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.AllowedMentions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.components.Component;
import org.example.rest.resources.partial.PartialAttachment;
import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
public interface StartThreadInForumChannel extends Auditable, MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    String name();

    @JsonProperty("auto_archive_duration")
    OptionalInt autoArchiveDuration();

    @JsonProperty("rate_limit_per_user")
    OptionalInt rateLimitPerUser();

    ForumThreadMessageParams message();

    @JsonProperty("applied_tags")
    Optional<List<Snowflake>> appliedTags();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/threads"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .files(files())
                .build();
    }

    @ImmutableJson

    interface ForumThreadMessageParams {

        static Builder builder() {
            return new Builder();
        }

        Optional<String> content();

        Optional<List<Message.Embed>> embeds();

        @JsonProperty("allowed_mentions")
        Optional<AllowedMentions> allowedMentions();

        Optional<List<Component>> components();

        @JsonProperty("sticker_ids")
        Optional<List<Snowflake>> stickerIds();

        Optional<List<PartialAttachment>> attachments();

        Optional<EnumSet<Message.Flag>> flags();

        class Builder extends ImmutableStartThreadInForumChannel.ForumThreadMessageParams.Builder {
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableStartThreadInForumChannel.Builder {
        protected Builder() {
        }
    }
}
