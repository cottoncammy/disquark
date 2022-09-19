package org.example.rest.request;

import io.vertx.uritemplate.Variables;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Default;

import java.util.Optional;

@Immutable
public interface Request extends Auditable, MultipartRequest {

    Endpoint endpoint();

    @Default
    default Variables variables() {
        return Variables.variables();
    }

    Optional<Object> body();

    default Optional<String> contentType() {
        if (files().isPresent()) {
            return Optional.of("multipart/form-data");
        } else if (body().isPresent()) {
            return Optional.of("application/json");
        }
        return Optional.empty();
    }
}
