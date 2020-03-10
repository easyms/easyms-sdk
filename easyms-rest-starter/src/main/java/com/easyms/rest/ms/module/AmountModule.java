package com.easyms.rest.ms.module;

import com.easyms.rest.ms.utils.AmountUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author khames.
 */
public class AmountModule extends SimpleModule {

    public AmountModule() {
        super();
        this.addSerializer(BigDecimal.class, new BigDecimalJsonSerializer());
        this.addDeserializer(BigDecimal.class, new BigDecimalJsonDeserializer());
    }

    public class BigDecimalJsonSerializer extends JsonSerializer<BigDecimal> {

        @Override
        public void serialize(BigDecimal value, JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(AmountUtils.rounded(value).toString());
        }
    }

    public class BigDecimalJsonDeserializer extends JsonDeserializer<BigDecimal> {

        @Override
        public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return AmountUtils.rounded(jsonParser.getValueAsString());

        }
    }

}
