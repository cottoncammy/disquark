package io.disquark.it;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import io.disquark.rest.AuthenticatedDiscordClient;
import io.disquark.rest.DiscordBotClient;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

public class DisQuarkTestTemplateProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(invocationContext(DiscordClients.getBotClient()), invocationContext(DiscordClients.getOAuth2Client()));
    }

    private TestTemplateInvocationContext invocationContext(AuthenticatedDiscordClient<?> discordClient) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return String.format("Test template invocation with %s",
                        discordClient instanceof DiscordBotClient ? "DiscordBotClient" : "DiscordOAuth2Client");
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(parameterResolver(discordClient));
            }
        };
    }

    private ParameterResolver parameterResolver(AuthenticatedDiscordClient<?> discordClient) {
        return new ParameterResolver() {
            @Override
            public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
                    throws ParameterResolutionException {
                return true;
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
                    throws ParameterResolutionException {
                return discordClient;
            }
        };
    }
}
