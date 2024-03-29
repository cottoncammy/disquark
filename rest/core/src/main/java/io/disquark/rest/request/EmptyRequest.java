package io.disquark.rest.request;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;

public class EmptyRequest implements Request {
    private final Endpoint endpoint;
    @Nullable
    private final Variables variables;
    @Nullable
    private final String auditLogReason;

    private EmptyRequest(HttpMethod httpMethod, String uri, boolean requiresAuthentication, Variables variables,
            String auditLogReason) {
        this.endpoint = Endpoint.create(httpMethod, uri, requiresAuthentication);
        this.variables = variables;
        this.auditLogReason = auditLogReason;
    }

    public EmptyRequest(HttpMethod httpMethod, String uri, boolean requiresAuthentication, Variables variables) {
        this(httpMethod, uri, requiresAuthentication, variables, null);
    }

    public EmptyRequest(HttpMethod httpMethod, String uri, Variables variables, String auditLogReason) {
        this(httpMethod, uri, true, variables, auditLogReason);
    }

    public EmptyRequest(HttpMethod httpMethod, String uri, Variables variables) {
        this(httpMethod, uri, variables, null);
    }

    public EmptyRequest(String uri, boolean requiresAuthentication, Variables variables) {
        this(HttpMethod.GET, uri, requiresAuthentication, variables);
    }

    public EmptyRequest(String uri, Variables variables) {
        this(HttpMethod.GET, uri, variables);
    }

    public EmptyRequest(String uri) {
        this(uri, null);
    }

    @Override
    public Optional<String> auditLogReason() {
        return Optional.ofNullable(auditLogReason);
    }

    @Override
    public List<FileUpload> files() {
        return Collections.emptyList();
    }

    @Override
    public Endpoint endpoint() {
        return endpoint;
    }

    @Override
    public Variables variables() {
        return variables != null ? variables : Request.super.variables();
    }

    @Override
    public Optional<Object> body() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return new Request.Builder().from(this).build().toString();
    }
}
