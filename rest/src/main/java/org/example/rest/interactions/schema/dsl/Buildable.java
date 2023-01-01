package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.CompletableInteraction;
import org.example.rest.interactions.schema.InteractionSchema;

public interface Buildable<D, C extends CompletableInteraction<D>> {

    InteractionSchema<D, C> schema();
}
