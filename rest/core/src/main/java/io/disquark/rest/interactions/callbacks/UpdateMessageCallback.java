package io.disquark.rest.interactions.callbacks;

import java.util.EnumSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.json.message.AllowedMentions;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.message.MessageEmbed;
import io.disquark.rest.json.message.PartialAttachment;
import io.disquark.rest.json.messagecomponent.Component;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class UpdateMessageCallback extends MultipartCallback<Interaction.MessageComponentData> {

    public abstract NullableOptional<Boolean> tts();

    public abstract NullableOptional<String> content();

    public abstract NullableOptional<List<MessageEmbed>> embeds();

    @JsonProperty("allowed_mentions")
    public abstract NullableOptional<AllowedMentions> allowedMentions();

    public abstract NullableOptional<EnumSet<Message.Flag>> flags();

    public abstract NullableOptional<List<Component>> components();

    public abstract NullableOptional<List<PartialAttachment>> attachments();

    // use a fake request object so we can reuse codec
    @Override
    protected Request toRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, ""))
                .body(this)
                .files(files())
                .build();
    }

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.UPDATE_MESSAGE).withData(this);
    }
}
