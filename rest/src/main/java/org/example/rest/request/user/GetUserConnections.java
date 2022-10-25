package org.example.rest.request.user;

import io.vertx.core.http.HttpMethod;
import org.example.rest.request.Requestable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;

public class GetUserConnections implements Requestable {

    @Override
    public Request asRequest() {
        return Request.builder().endpoint(Endpoint.create(HttpMethod.GET, "/users/@me/connections")).build();
    }
}
