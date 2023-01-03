package org.example.it.extension;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

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
        return Stream.of();
    }

    private TestTemplateInvocationContext idk() {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return TestTemplateInvocationContext.super.getDisplayName(invocationIndex);
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(new SomeExtension2());
            }
        };
    }
}
