package com.easyms.logging.ms;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LoggerNameConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        String prefix = "custom-";
        return iLoggingEvent.getLoggerName().startsWith(prefix) ? iLoggingEvent.getLoggerName().substring(prefix.length()) : "logs";
    }
}
