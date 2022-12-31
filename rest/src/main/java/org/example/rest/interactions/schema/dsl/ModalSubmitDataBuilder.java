package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModalSubmitDataBuilder extends AbstractStage<ModalSubmitBuilder> implements InteractionSchema<Interaction.ModalSubmitData> {
    private final List<MessageComponentDataBuilder> components = new ArrayList<>();

    @Nullable
    private String customId;

    protected ModalSubmitDataBuilder(ModalSubmitBuilder previousStage) {
        super(previousStage);
    }

    public ModalSubmitDataBuilder customId(String customId) {
        this.customId = customId;
        return this;
    }

    public ModalSubmitDataBuilder with(MessageComponentDataBuilder component) {
        components.add(component);
        return this;
    }

    @Override
    public boolean validate(Interaction<Interaction.ModalSubmitData> interaction) {
        return previousStage.validate(interaction) &&
                interaction.data().isPresent() &&
                Objects.equals(customId, interaction.data().get().customId());
    }
}
