package org.example.rest.resources.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;

@ImmutableJson
public interface UpdateApplicationRoleConnectionMetadataRecords extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonValue
    List<ApplicationRoleConnectionMetadata> applicationRoleConnectionMetadataRecords();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/role-connections/metadata"))
                .variables(Variables.variables().set("application.id", applicationId().getValueAsString()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableUpdateApplicationRoleConnectionMetadataRecords.Builder {
        protected Builder() {}
    }
}
