package org.example.rest.request;

import io.vertx.mutiny.core.Promise;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableStyle;
import org.example.rest.response.Response;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Default;

import java.util.Optional;

@Immutable
@ImmutableStyle
public interface Request extends Auditable, MultipartRequest {

    static Builder builder() {
        return new Builder();
    }

    Endpoint endpoint();

    @Default
    default Variables variables() {
        return Variables.variables();
    }

    Optional<Object> body();

    default Promise<Response> responsePromise() {
        return Promise.promise();
    }

    default Optional<String> contentType() {
        if (files().isPresent()) {
            return Optional.of("multipart/form-data");
        } else if (body().isPresent()) {
            return Optional.of("application/json");
        }
        return Optional.empty();
    }

    class Builder extends ImmutableRequest.Builder {
        protected Builder() {}
    }
}
