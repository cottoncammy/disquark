package org.example.rest.request;

import java.util.Optional;

import io.vertx.mutiny.uritemplate.Variables;

import org.example.immutables.ImmutableBuilder;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Redacted;

@ImmutableBuilder
public interface Request extends Auditable, MultipartRequest {

    static Builder builder() {
        return new Builder();
    }

    Endpoint endpoint();

    @Default
    @Redacted
    default Variables variables() {
        return Variables.variables();
    }

    Optional<Object> body();

    default Optional<String> contentType() {
        if (!files().isEmpty()) {
            return Optional.of("multipart/form-data");
        } else if (body().isPresent()) {
            return Optional.of("application/json");
        }
        return Optional.empty();
    }

    class Builder extends ImmutableRequest.Builder {
        protected Builder() {
        }
    }
}
