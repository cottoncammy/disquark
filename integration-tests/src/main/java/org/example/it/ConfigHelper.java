package org.example.it;

import org.eclipse.microprofile.config.ConfigProvider;

import java.util.Optional;

public class ConfigHelper {

    public static <T> T configValue(String name, Class<T> type) {
        return ConfigProvider.getConfig().getValue(name, type);
    }

    public static <T> Optional<T> optionalConfigValue(String name, Class<T> type) {
        return ConfigProvider.getConfig().getOptionalValue(name, type);
    }

    private ConfigHelper() {}
}
