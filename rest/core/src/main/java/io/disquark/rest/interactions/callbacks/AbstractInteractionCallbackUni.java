package io.disquark.rest.interactions.callbacks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.interactions.RespondedInteraction;
import io.disquark.rest.json.interaction.Interaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.AbstractUni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.mutiny.ext.web.RoutingContext;

import org.immutables.value.Value.NonAttribute;

abstract class AbstractInteractionCallbackUni<T> extends AbstractUni<RespondedInteraction<T>> {

    @JsonIgnore
    public abstract RoutingContext context();

    // no type parameters, Immutables bug
    @JsonIgnore
    public abstract Interaction interaction();

    @JsonIgnore
    public abstract DiscordInteractionsClient<?> interactionsClient();

    // TODO support file uploads & use codecs
    @JsonIgnore
    protected Uni<Void> serialize() {
        return context().json(toResponse()).invoke(() -> context().put("responded", true));
    }

    // TODO this is bugged
    @Override
    public void subscribe(UniSubscriber<? super RespondedInteraction<T>> downstream) {
        serialize()
                .replaceWith(new RespondedInteraction<>((Interaction<T>) interaction(), interactionsClient()))
                .subscribe().withSubscriber(downstream);
    }

    @JsonIgnore
    @NonAttribute
    protected abstract Interaction.Response<?> toResponse();
}
