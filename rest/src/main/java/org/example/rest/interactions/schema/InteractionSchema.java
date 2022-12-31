package org.example.rest.interactions.schema;

import org.example.rest.interactions.schema.dsl.*;
import org.example.rest.resources.interactions.Interaction;

public interface InteractionSchema<T> {

    static InteractionSchema<Void> ping() {
        return interaction -> interaction.type() == Interaction.Type.PING;
    }

    static ApplicationCommandBuilder applicationCommand() {
        return new ApplicationCommandBuilder();
    }

    static MessageComponentBuilder messageComponent() {
        return new MessageComponentBuilder();
    }

    static ModalSubmitBuilder modalSubmit() {
        return new ModalSubmitBuilder();
    }

    boolean validate(Interaction<T> interaction);
}
