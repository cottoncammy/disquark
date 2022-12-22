package org.example.rest.resources.channel.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.*;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.AllowedMentions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.components.Component;
import org.immutables.value.Value.Enclosing;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

// TODO deepImmutablesDetection ?
@Enclosing
@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
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
                .variables(Variables.variables().set("channel.id", channelId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .files(files())
                .build();
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
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

        Optional<List<Message.Attachment>> attachments();

        Optional<EnumSet<Message.Flag>> flags();

        class Builder extends ImmutableStartThreadInForumChannel.ForumThreadMessageParams.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableStartThreadInForumChannel.Builder {
        protected Builder() {}
    }
}
