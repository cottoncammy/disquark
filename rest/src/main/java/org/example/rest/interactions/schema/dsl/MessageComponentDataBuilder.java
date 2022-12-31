package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import javax.annotation.Nullable;
import java.util.Objects;

// TODO differentiate between method component and dsl component
public class MessageComponentDataBuilder extends AbstractStage<MessageComponentBuilder> implements InteractionSchema<Interaction.MessageComponentData> {
    @Nullable
    private String customId;
    @Nullable
    private Component.Type type;

    protected MessageComponentDataBuilder(MessageComponentBuilder previousStage) {
        super(previousStage);
    }

    public static MessageComponentDataBuilder component() {
        return new MessageComponentDataBuilder();
    }

    public MessageComponentDataBuilder customId(String customId) {
        this.customId = customId;
        return this;
    }

    public MessageComponentDataBuilder type(Component.Type type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean validate(Interaction<Interaction.MessageComponentData> interaction) {
        return previousStage.validate(interaction) &&
                interaction.data().isPresent() &&
                Objects.equals(customId, interaction.data().get().customId()) &&
                Objects.equals(type, interaction.data().get().componentType());
    }
}
