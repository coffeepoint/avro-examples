package com.coffeepoint.avro;


import org.apache.avro.*;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

public class PricingDecimalConvertor  extends Conversions.DecimalConversion {
    private Schema bigDecimalSchema = new Schema.Parser().parse("{\n" +
            "  \"type\": \"bytes\",\n" +
            "  \"logicalType\": \"decimal\",\n" +
            "  \"precision\": 30,\n" +
            "  \"scale\": 15\n" +
            "} ");

    @Override
    public Schema getRecommendedSchema() {
        return bigDecimalSchema;
    }

    public ByteBuffer toBytes(BigDecimal value, Schema schema, LogicalType type) {
        int scale = ((LogicalTypes.Decimal)type).getScale();

        return super.toBytes(value.setScale(scale), schema, type);

    }
}
