package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;

public class ModalSubmitBuilder extends InteractionBuilder<ModalSubmitDataBuilder> implements InteractionSchema<Interaction.ModalSubmitData> {

    @Override
    public ModalSubmitDataBuilder data() {
        return new ModalSubmitDataBuilder(this);
    }

    @Override
    public boolean validate(Interaction<Interaction.ModalSubmitData> interaction) {
        return guildIdValidator.test(interaction.guildId());
    }
}
