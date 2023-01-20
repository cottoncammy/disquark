package org.example.rest.request;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Auditable {

    @JsonIgnore
    Optional<String> auditLogReason();
}
