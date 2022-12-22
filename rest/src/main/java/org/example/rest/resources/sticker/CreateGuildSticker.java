package org.example.rest.resources.sticker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.*;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface CreateGuildSticker extends Auditable, MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    String name();

    String description();

    String tags();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/stickers"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .files(files())
                .build();
    }

    class Builder extends ImmutableCreateGuildSticker.Builder {
        protected Builder() {}
    }
}
