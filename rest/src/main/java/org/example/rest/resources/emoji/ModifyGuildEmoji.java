package org.example.rest.resources.emoji;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;
import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface ModifyGuildEmoji extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake emojiId();

    Optional<String> name();

    Optional<List<Snowflake>> roles();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/emojis/{emoji.id}"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()).set("emoji.id", emojiId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuildEmoji.Builder {
        protected Builder() {}
    }
}
