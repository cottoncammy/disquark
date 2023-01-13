package org.example.rest.resources.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.jackson.ImageDataSerializer;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.nullableoptional.NullableOptional;

import java.util.Optional;

@ImmutableJson
public interface ModifyCurrentUser extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    Optional<String> username();

    @JsonSerialize(using = ImageDataSerializer.class)
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<Buffer> avatar();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/users/@me"))
                .body(this)
                .build();
    }

    class Builder extends ImmutableModifyCurrentUser.Builder {
        protected Builder() {}
    }
}
