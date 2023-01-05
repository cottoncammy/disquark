package org.example.it;

import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.DiscordBotClient;
import org.junit.jupiter.api.extension.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class SomeExtension implements TestTemplateInvocationContextProvider {

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
            public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return true;
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                return discordClient;
            }
        };
    }
}