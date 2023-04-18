package io.disquark.rest.request;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nullable;

import io.disquark.rest.json.Snowflake;
import io.vertx.mutiny.core.buffer.Buffer;

public class FileUpload {
    private final String name;
    private final Buffer content;
    @Nullable
    private final Snowflake id;

    private FileUpload(String name, Buffer content, Snowflake id) {
        this.name = requireNonNull(name, "name");
        this.content = requireNonNull(content, "content");
        this.id = id;
    }

    public FileUpload(String name, Buffer content) {
        this(name, content, null);
    }

    public FileUpload withId(Snowflake id) {
        return new FileUpload(name, content, requireNonNull(id, "id"));
    }

    public String getName() {
        return name;
    }

    public Buffer getContent() {
        return content;
    }

    public Optional<Snowflake> getId() {
        return Optional.ofNullable(id);
    }
}
