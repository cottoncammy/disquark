package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;

public class MessageComponentBuilder extends InteractionBuilder<MessageComponentDataBuilder> implements InteractionSchema<Interaction.MessageComponentData> {

    @Override
    public MessageComponentDataBuilder data() {
        return new MessageComponentDataBuilder(this);
    }

    @Override
    public boolean validate(Interaction<Interaction.MessageComponentData> interaction) {
        return guildIdValidator.test(interaction.guildId());
    }
}
