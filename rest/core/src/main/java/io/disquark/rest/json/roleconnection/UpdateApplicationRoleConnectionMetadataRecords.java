package io.disquark.rest.json.roleconnection;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.vertx.core.http.HttpMethod;

@ImmutableMulti
abstract class UpdateApplicationRoleConnectionMetadataRecords extends AbstractRequestMulti<ApplicationRoleConnection.Metadata> {

    public abstract Snowflake applicationId();

    public abstract List<ApplicationRoleConnection.Metadata> records();

    @Override
    public void subscribe(Flow.Subscriber<? super ApplicationRoleConnection.Metadata> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(ApplicationRoleConnection.Metadata[].class))
                .onItem().<ApplicationRoleConnection.Metadata> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/role-connections/metadata"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(records())
                .build();
    }
}
