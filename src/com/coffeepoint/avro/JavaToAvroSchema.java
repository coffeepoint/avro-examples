package com.coffeepoint.avro;

import org.apache.avro.Conversions;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import sun.net.www.content.text.Generic;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class JavaToAvroSchema {

    public static void main(String[] args) throws IOException {
        ReflectData  reflectData = ReflectData.AllowNull.get();
        reflectData.addLogicalTypeConversion(new PricingDecimalConvertor() );
        Schema genericPriceSchema = reflectData.getSchema(GenericPrice.class);
        System.out.println(genericPriceSchema.toString(true));

        // Serialize user1 and user2 to disk
        File file = new File("genericPrice.avro");
        // create a file of packets
        DatumWriter<GenericPrice> writer = new ReflectDatumWriter<GenericPrice>(GenericPrice.class, reflectData);
        DataFileWriter<GenericPrice> out = new DataFileWriter<GenericPrice>(writer)
                .setCodec(CodecFactory.deflateCodec(9))
                .create(genericPriceSchema, file);
        out.append(genericPrice() );

        // close the output file
        out.close();

        // open a file of packets
        DatumReader<GenericPrice> reader = new ReflectDatumReader<GenericPrice>(genericPriceSchema);
        DataFileReader<GenericPrice> in = new DataFileReader<GenericPrice>(file, reader);

        // read 100 packets from the file & print them as JSON
         for (GenericPrice price : in) {
            System.out.println(ReflectData.get().toString(price));
        }

        // close the input file
        in.close();

        /*
        DatumWriter<GenericPrice> datumWriter = new SpecificDatumWriter<GenericPrice>( genericPriceSchema);
        DataFileWriter<GenericPrice> dataFileWriter = new DataFileWriter<GenericPrice>(datumWriter);
        dataFileWriter.create(genericPriceSchema, file);
        dataFileWriter.append(genericPrice());
        dataFileWriter.close();
        */
    }

    private static GenericPrice genericPrice() {
        GenericPrice genericPrice = new GenericPrice();
        genericPrice.setId("Id");
        genericPrice.setIdType("ISIN");
        GenericValue genericValue = new GenericValue();
        genericValue.setAsk(new BigDecimal("100.1000000000000000001"));
        genericValue.setMid(new BigDecimal("100.0"));
        genericValue.setBid(new BigDecimal("99.9"));
        genericPrice.setPrice(genericValue);
        GenericValue yields = new GenericValue();
        yields.setBid(new BigDecimal("2.0"));
        yields.setMid(new BigDecimal("1.9"));
        yields.setAsk(new BigDecimal("1.8"));
        genericPrice.setYield(yields);
        GenericSpread spread = new GenericSpread();
        spread.setBenchmarkId("BenchmarkId");
        spread.setBenchmarkType("ISIN");
        GenericValue spreadValues = new GenericValue();
        spreadValues.setAsk(new BigDecimal("0.1"));
        spreadValues.setMid(new BigDecimal("0.2"));
        spreadValues.setBid(new BigDecimal("0.15"));
        spread.setGenericValue(spreadValues);
        genericPrice.setSpread(spread);
        return genericPrice;
    }
}
