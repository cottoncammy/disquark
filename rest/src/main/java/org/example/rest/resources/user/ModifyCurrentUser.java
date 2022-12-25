package org.example.rest.resources.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;

import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface ModifyCurrentUser extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    Optional<String> username();

    Optional<String> avatar();

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
