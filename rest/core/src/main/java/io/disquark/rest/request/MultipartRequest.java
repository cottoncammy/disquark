package io.disquark.rest.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.immutables.value.Value.Redacted;

public interface MultipartRequest {

    @Redacted
    @JsonIgnore
    List<FileUpload> files();
}
