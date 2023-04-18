package io.disquark.rest.interactions.callbacks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.disquark.rest.request.Codecs;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.Uni;

import org.immutables.value.Value.NonAttribute;

abstract class MultipartCallback<T> extends AbstractInteractionCallbackUni<T> implements MultipartRequest {

    @JsonIgnore
    @NonAttribute
    protected abstract Request toRequest();

    @Override
    @JsonIgnore
    protected Uni<Void> serialize() {
        if (files().isEmpty()) {
            return context().json(toResponse());
        }

        return context().response().send(Codecs.getCodec("multipart/form-data")
                .serialize(toRequest(), context().response().headers()).asPublisher().get());
    }
}
