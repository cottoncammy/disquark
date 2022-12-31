package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;

public class ApplicationCommandBuilder extends InteractionBuilder<ApplicationCommandDataBuilder> implements InteractionSchema<Interaction.ApplicationCommandData> {

    @Override
    public ApplicationCommandDataBuilder data() {
        return new ApplicationCommandDataBuilder(this);
    }

    @Override
    public boolean validate(Interaction<Interaction.ApplicationCommandData> interaction) {
        return guildIdValidator.test(interaction.guildId());
    }
}
