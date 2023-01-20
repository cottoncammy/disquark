package org.example.rest.util;

import java.util.Objects;

import io.vertx.mutiny.uritemplate.Variables;

public class UriTemplate {
    private final String uri;
    private final io.vertx.mutiny.uritemplate.UriTemplate delegate;

    public static UriTemplate of(String uri) {
        return new UriTemplate(uri, io.vertx.mutiny.uritemplate.UriTemplate.of(uri));
    }

    private UriTemplate(String uri, io.vertx.mutiny.uritemplate.UriTemplate delegate) {
        this.uri = uri;
        this.delegate = delegate;
    }

    private String getUriWithoutQueryTemplate() {
        // hack, we assume that there's only one query template and that it's the last template in the string
        if (uri.contains("{?")) {
            return uri.substring(0, uri.lastIndexOf('{'));
        }

        return uri;
    }

    public String getUri() {
        return uri;
    }

    public String expandToString(Variables variables) {
        return delegate.expandToString(variables);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UriTemplate that = (UriTemplate) o;
        return getUriWithoutQueryTemplate().equals(that.getUriWithoutQueryTemplate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUriWithoutQueryTemplate());
    }

    @Override
    public String toString() {
        return "UriTemplate{" +
                "uri=\"" + getUriWithoutQueryTemplate() + "\"" +
                '}';
    }
}
