package io.disquark.rest.resources.application;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

@ImmutableMulti
abstract class UpdateApplicationRoleConnectionMetadataRecords extends AbstractRequestMulti<ApplicationRoleConnectionMetadata> {

    public abstract Snowflake applicationId();

    public abstract List<ApplicationRoleConnectionMetadata> applicationRoleConnectionMetadataRecords();

    @Override
    public void subscribe(Flow.Subscriber<? super ApplicationRoleConnectionMetadata> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(ApplicationRoleConnectionMetadata[].class))
                .onItem().<ApplicationRoleConnectionMetadata> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/role-connections/metadata"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(applicationRoleConnectionMetadataRecords())
                .build();
    }
}
