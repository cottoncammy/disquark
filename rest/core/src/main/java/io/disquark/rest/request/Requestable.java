package io.disquark.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

@FunctionalInterface
public interface Requestable {

    @JsonIgnore
    Request asRequest();
}
