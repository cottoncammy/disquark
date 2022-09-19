package org.example.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Optional;

public interface Auditable {

    @JsonIgnore
    Optional<String> auditLogReason();
}
