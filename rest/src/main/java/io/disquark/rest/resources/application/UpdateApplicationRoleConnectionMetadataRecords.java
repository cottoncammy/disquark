package io.disquark.rest.resources.application;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

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
        protected Builder() {
        }
    }
}
