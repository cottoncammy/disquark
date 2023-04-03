package io.disquark.rest.util;

import static io.smallrye.mutiny.unchecked.Unchecked.consumer;

import java.util.Properties;

import io.smallrye.mutiny.Uni;

public class UserAgentProvider {

    public static Uni<String> getUserAgent() {
        return Uni.createFrom().item(new Properties())
                .invoke(consumer(props -> props
                        .load(Thread.currentThread().getContextClassLoader().getResourceAsStream("maven.properties"))))
                .map(props -> props.getProperty("project.version"))
                .map(version -> String.format("DiscordBot (%s, %s)", "https://github.com/disquark/disquark", version))
                .memoize().indefinitely();
    }

    private UserAgentProvider() {
    }
}
