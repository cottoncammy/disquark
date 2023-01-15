package org.example.rest.util;

import io.vertx.core.json.JsonObject;

import static java.util.Objects.requireNonNull;

public class Variables {

    public static io.vertx.mutiny.uritemplate.Variables variables(String k1, Object v1) {
        return io.vertx.mutiny.uritemplate.Variables.variables(JsonObject.of(k1, v1));
    }

    public static io.vertx.mutiny.uritemplate.Variables variables(String k1, Object v1, String k2, Object v2) {
        JsonObject json = JsonObject.of(k1, v1, k2, v2);
        return io.vertx.mutiny.uritemplate.Variables.variables(json);
    }

    public static io.vertx.mutiny.uritemplate.Variables variables(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        JsonObject json = JsonObject.of(k1, v1, k2, v2, k3, v3);
        return io.vertx.mutiny.uritemplate.Variables.variables(json);
    }

    public static io.vertx.mutiny.uritemplate.Variables variables(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        JsonObject json = JsonObject.of(k1, v1, k2, v2, k3, v3, k4, v4);
        return io.vertx.mutiny.uritemplate.Variables.variables(json);
    }

    private Variables() {}
}
