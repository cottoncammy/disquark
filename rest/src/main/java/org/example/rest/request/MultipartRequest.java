package org.example.rest.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

public interface MultipartRequest {

    @Redacted
    @JsonIgnore
    List<Map.Entry<String, Buffer>> files();
}
