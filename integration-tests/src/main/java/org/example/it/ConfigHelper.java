package org.example.it;

import org.eclipse.microprofile.config.ConfigProvider;

public class ConfigHelper {

    public static <T> T configValue(String name, Class<T> type) {
        return ConfigProvider.getConfig().getValue(name, type);
    }

    private ConfigHelper() {}
}
