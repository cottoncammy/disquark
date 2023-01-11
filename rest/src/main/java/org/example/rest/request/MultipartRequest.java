package org.example.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.mutiny.core.buffer.Buffer;
import org.immutables.value.Value.Auxiliary;

import java.util.List;
import java.util.Map;

public interface MultipartRequest {

    @Auxiliary
    @JsonIgnore
    List<Map.Entry<String, Buffer>> files();
}
