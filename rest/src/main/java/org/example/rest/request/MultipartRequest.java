package org.example.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.mutiny.core.buffer.Buffer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MultipartRequest {

    @JsonIgnore
    Optional<List<Map.Entry<String, Buffer>>> files();
}
