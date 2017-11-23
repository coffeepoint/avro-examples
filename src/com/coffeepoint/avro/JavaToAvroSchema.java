package com.coffeepoint.avro;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.avro.Conversions;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
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

        // Generate a Schema from a Java Class
        generateSchemaFromJavaClass(GenericPrice.class,"genericPrice.avsc");

        // Read a schema from File
        Schema genericPriceSchema =  new Schema.Parser().parse(new File("genericPrice.avsc"));

        // Write prices with Schema
        writePricesWithSchema(genericPriceSchema, "genericPricesWithSchema.avro", 0);

        // Read prices with Schema
        readPricesWithSchema(genericPriceSchema, "genericPricesWithSchema.avro");

        // Write prices without Schema
        writePricesWithoutSchema(genericPriceSchema, "genericPricesWithoutSchema.avro", 0);

        // Read prices with Schema
        readPricesWithoutSchema(genericPriceSchema, "genericPricesWithoutSchema.avro");
    }

    private static void readPricesWithSchema(Schema genericPriceSchema, String avroFileName) throws IOException {
        // Read prices with Schema
        DatumReader<GenericPrice> reader = new ReflectDatumReader<>(genericPriceSchema,genericPriceSchema,reflectDataWithNullableAndPricingDecimalConvertor());
        DataFileReader<GenericPrice> in = new DataFileReader<>(new File(avroFileName), reader);

        // read 100 packets from the file & print them as JSON
        for (GenericPrice price : in) {
           System.out.println(price);
       }

        // close the input file
        in.close();
    }

    private static void readPricesWithoutSchema(Schema genericPriceSchema, String avroFileName) throws IOException {
        // Read prices with Schema
        DatumReader<GenericPrice> reader = new ReflectDatumReader<>(genericPriceSchema,genericPriceSchema,reflectDataWithNullableAndPricingDecimalConvertor());
        FileInputStream fileInputStream = new FileInputStream(avroFileName);

        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(fileInputStream, null);

        GenericPrice genericPrice = null;

        while (!decoder.isEnd()) {
           genericPrice = reader.read(null,decoder);
           System.out.println(genericPrice);
        }


        // close the input file
        fileInputStream.close();
    }

    private static void writePricesWithSchema(Schema genericPriceSchema, String avroFileName, int numberOfPricesToGenerate)
        throws IOException {
        // Serialize some generic prices with schema
        File file = new File(avroFileName);
        // create a file of packets

        DatumWriter<GenericPrice> writer = new ReflectDatumWriter<>(GenericPrice.class, reflectDataWithNullableAndPricingDecimalConvertor());
        DataFileWriter<GenericPrice> out = new DataFileWriter<>(writer)
                .setCodec(CodecFactory.deflateCodec(9))
                .create(genericPriceSchema, file);

        for (int i=0; i<numberOfPricesToGenerate; ++i) {
            out.append(genericPrice());
        }

        // close the output file
        out.close();
    }


    private static void writePricesWithoutSchema(Schema genericPriceSchema, String avroFileName, int numberOfPricesToGenerate)
        throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(avroFileName);
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(fileOutputStream, null);

        DatumWriter<GenericPrice> writer = new ReflectDatumWriter<>(GenericPrice.class, reflectDataWithNullableAndPricingDecimalConvertor());

        for (int i=0; i<numberOfPricesToGenerate; ++i) {
            writer.write(genericPrice(),encoder);
        }

        encoder.flush();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private static GenericPrice genericPrice() {
        GenericPrice genericPrice = new GenericPrice();
        genericPrice.setId("Id");
        genericPrice.setIdType("ISIN");
        GenericValue genericValue = new GenericValue();
        genericValue.setAsk(new BigDecimal(90 + Math.random()*10));
        genericValue.setMid(new BigDecimal(90 + Math.random()*10));
        genericValue.setBid(new BigDecimal(90 + Math.random()*10));
        genericPrice.setPrice(genericValue);
        GenericValue yields = new GenericValue();
        yields.setBid(new BigDecimal(Math.random()*5));
        yields.setMid(new BigDecimal(Math.random()*5));
        yields.setAsk(new BigDecimal(Math.random()*5));
        genericPrice.setYield(yields);
        GenericSpread spread = new GenericSpread();
        spread.setBenchmarkId("BenchmarkId");
        spread.setBenchmarkType("ISIN");
        GenericValue spreadValues = new GenericValue();
        spreadValues.setAsk(new BigDecimal(Math.random()));
        spreadValues.setMid(new BigDecimal(Math.random()));
        spreadValues.setBid(new BigDecimal(Math.random()));
        spread.setGenericValue(spreadValues);
        genericPrice.setSpread(spread);
        return genericPrice;
    }

    private static void generateSchemaFromJavaClass(Class clazz, String schemaFileName)
        throws IOException {
        ReflectData  reflectData = reflectDataWithNullableAndPricingDecimalConvertor();

        Schema genericPriceSchema = reflectData.getSchema(clazz);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(schemaFileName)))
        {
            writer.write(genericPriceSchema.toString(true));
        }
    }

    private static ReflectData reflectDataWithNullableAndPricingDecimalConvertor() {
        ReflectData  reflectData = ReflectData.AllowNull.get();
        reflectData.addLogicalTypeConversion(new PricingDecimalConvertor() );
        return reflectData;
    }
}
