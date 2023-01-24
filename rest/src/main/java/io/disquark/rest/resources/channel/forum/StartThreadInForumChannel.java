package io.disquark.rest.resources.channel.forum;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Auditable;
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
