package com.easyms.logging.ms;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import com.agido.logback.elasticsearch.ClassicElasticsearchPublisher;
import com.agido.logback.elasticsearch.config.ElasticsearchProperties;
import com.agido.logback.elasticsearch.config.HttpRequestHeaders;
import com.agido.logback.elasticsearch.config.Settings;
import com.agido.logback.elasticsearch.util.ErrorReporter;
import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.marker.LogstashMarker;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.Iterator;

public class CustomClassicElasticSearchPublisher extends ClassicElasticsearchPublisher {
    public CustomClassicElasticSearchPublisher(Context context, ErrorReporter errorReporter, Settings settings, ElasticsearchProperties properties, HttpRequestHeaders headers) throws IOException {
        super(context, errorReporter, settings, properties, headers);
    }

    @Override
    protected void serializeCommonFields(JsonGenerator gen, ILoggingEvent event) throws IOException {
        super.serializeCommonFields(gen, event);
        writeLogstashMarkerIfNecessary(gen, event.getMarker());
    }

    private void writeLogstashMarkerIfNecessary(JsonGenerator generator, Marker marker) throws IOException {
        if (marker != null && marker instanceof LogstashMarker) {
            ((LogstashMarker) marker).writeTo(generator);

            if (((LogstashMarker) marker).hasReferences()) {
                for (Iterator<?> i = ((LogstashMarker) marker).iterator(); i.hasNext(); ) {
                    Marker next = (Marker) i.next();
                    writeLogstashMarkerIfNecessary(generator, next);
                }
            }
        }
    }
}
