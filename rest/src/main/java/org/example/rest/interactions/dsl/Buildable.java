package org.example.rest.interactions.dsl;

import org.example.rest.interactions.CompletableInteraction;

public interface Buildable<D, C extends CompletableInteraction<D>> {

    InteractionSchema<D, C> schema();
}
