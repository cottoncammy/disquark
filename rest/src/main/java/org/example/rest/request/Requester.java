package org.example.rest.request;

import io.vertx.core.Context;
import io.vertx.core.Future;
import org.example.rest.response.Response;

public interface Requester {

    Future<Response> request(Requestable requestable, Context context);
}
