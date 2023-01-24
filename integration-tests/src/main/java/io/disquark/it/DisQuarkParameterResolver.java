package io.disquark.it;

import java.util.Optional;

import io.disquark.it.config.ConfigHelper;
import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.oauth2.DiscordOAuth2Client;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class DisQuarkParameterResolver implements ParameterResolver {

    private Optional<?> configValue(ParameterContext context) {
        return context.findAnnotation(ConfigValue.class)
                .flatMap(annotation -> ConfigHelper.optionalConfigValue(annotation.value(), context.getParameter().getType()));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Class<?> cl = parameterContext.getParameter().getType();
        return cl == DiscordBotClient.class || cl == DiscordOAuth2Client.class || configValue(parameterContext).isPresent();
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Class<?> cl = parameterContext.getParameter().getType();
        return cl == DiscordBotClient.class ? DiscordClients.getBotClient()
                : cl == DiscordOAuth2Client.class ? DiscordClients.getOAuth2Client() : configValue(parameterContext).get();
    }
}
