package com.easyms.logging.ms;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.logstash.logback.marker.ObjectFieldsAppendingMarker;

import java.io.IOException;
import java.util.logging.Logger;

public class CustomObjectFieldsAppendingMarker extends ObjectFieldsAppendingMarker {

    private static Logger logger = Logger.getLogger(CustomObjectFieldsAppendingMarker.class.getName());
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public CustomObjectFieldsAppendingMarker(Object object) {
        super(object);
    }

    @Override
    public void writeTo(JsonGenerator generator) throws IOException {
        if (generator.getCodec() == null) {
            generator.setCodec(objectMapper);
        }
        super.writeTo(generator);
    }
}
