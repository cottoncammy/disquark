package org.example.it.config;

import java.util.Optional;

import org.eclipse.microprofile.config.ConfigProvider;

public class ConfigHelper {

    public static <T> T configValue(String name, Class<T> type) {
        return ConfigProvider.getConfig().getValue(name, type);
    }

    public static <T> Optional<T> optionalConfigValue(String name, Class<T> type) {
        return ConfigProvider.getConfig().getOptionalValue(name, type);
    }

    private ConfigHelper() {
    }
}
