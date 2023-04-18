package io.disquark.rest.json.user;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class ModifyCurrentUser extends AbstractRequestUni<User> {

    public abstract Optional<String> username();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Buffer> avatar();

    @Override
    public void subscribe(UniSubscriber<? super User> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(User.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/users/@me"))
                .body(this)
                .build();
    }
}
