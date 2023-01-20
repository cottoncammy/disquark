package org.example.rest.resources.application;

import static org.example.rest.util.Variables.variables;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface UpdateApplicationRoleConnectionMetadataRecords extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @Nullable
    @JsonIgnore
    Snowflake applicationId();

    @JsonValue
    List<ApplicationRoleConnectionMetadata> applicationRoleConnectionMetadataRecords();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/role-connections/metadata"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableUpdateApplicationRoleConnectionMetadataRecords.Builder {
        protected Builder() {}
    }
}
