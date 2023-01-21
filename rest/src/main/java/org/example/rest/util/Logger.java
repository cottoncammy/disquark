package org.example.rest.util;

import java.util.function.Consumer;

import org.slf4j.event.Level;

public class Logger {

    public static void log(org.slf4j.Logger logger, Level level, Consumer<org.slf4j.Logger> loggerConsumer) {
        if (logger.isEnabledForLevel(level)) {
            loggerConsumer.accept(logger);
        }
    }

    private Logger() {
    }
}
