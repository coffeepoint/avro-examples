package com.coffeepoint.avro.ado;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.avro.Conversions.DecimalConversion;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class AvroSerialisationFromGeneratedJava {

    public static void main(String[] args) throws IOException {


        // Write prices with Schema
        writePricesWithSchema("genericPricesWithSchema.avro", 100000);

        // Read prices with Schema
        readPricesWithSchema("genericPricesWithSchema.avro");

        // Write prices without Schema
        writePricesWithoutSchema("genericPricesWithoutSchema.avro", 100000);

        // Read prices with Schema
        readPricesWithoutSchema("genericPricesWithoutSchema.avro");

    }

    private static void readPricesWithSchema( String avroFileName) throws IOException {
        // Read prices with Schema
        SpecificData specificData = new SpecificData();
        specificData.addLogicalTypeConversion(new DecimalConversion());
        DatumReader<GenericPrice> reader = new SpecificDatumReader<GenericPrice>(new GenericPrice().getSchema(),new GenericPrice().getSchema(),specificData);
        DataFileReader<GenericPrice> in = new DataFileReader<>(new File(avroFileName), reader);

        // read 100 packets from the file & print them as JSON
        for (GenericPrice price : in) {
           System.out.println(price);
       }

        // close the input file
        in.close();
    }

    private static void readPricesWithoutSchema(String avroFileName) throws IOException {
        // Read prices with Schema
        SpecificData specificData = new SpecificData();
        specificData.addLogicalTypeConversion(new DecimalConversion());
        DatumReader<GenericPrice> reader = new SpecificDatumReader<GenericPrice>(new GenericPrice().getSchema(),new GenericPrice().getSchema(),specificData);
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


    private static void writePricesWithSchema( String avroFileName, int numberOfPricesToGenerate)
        throws IOException {
        // Serialize some generic prices with schema
        File file = new File(avroFileName);
        // create a file of packets
        SpecificData specificData = new SpecificData();
        specificData.addLogicalTypeConversion(new DecimalConversion());
        DatumWriter<GenericPrice> writer = new SpecificDatumWriter<GenericPrice>(new GenericPrice().getSchema(),specificData);

        DataFileWriter<GenericPrice> out = new DataFileWriter<>(writer)
                .setCodec(CodecFactory.deflateCodec(9))
                .create(new GenericPrice().getSchema(), file);

        for (int i=0; i<numberOfPricesToGenerate; ++i) {
            out.append(genericPrice());
        }

        // close the output file
        out.close();
    }


    private static void writePricesWithoutSchema( String avroFileName, int numberOfPricesToGenerate)
        throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(avroFileName);
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(fileOutputStream, null);

        SpecificData specificData = new SpecificData();
        specificData.addLogicalTypeConversion(new DecimalConversion());
        DatumWriter<GenericPrice> writer = new SpecificDatumWriter<GenericPrice>(new GenericPrice().getSchema(),specificData);

        for (int i=0; i<numberOfPricesToGenerate; ++i) {
            writer.write(genericPrice(),encoder);
        }

        encoder.flush();
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    private static GenericPrice genericPrice() {
        GenericPrice genericPrice = new GenericPrice();
        genericPrice.setId(new Long(Math.round(Math.random()*100000)).toString());
        genericPrice.setIdType("XS"+new Long(100000000+Math.round(Math.random()*100000)).toString());
        GenericValue genericValue = new GenericValue();
        genericValue.setAsk(new BigDecimal(90 + Math.random()*10).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        genericValue.setMid(new BigDecimal(90 + Math.random()*10).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        genericValue.setBid(new BigDecimal(90 + Math.random()*10).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        genericPrice.setPrice(genericValue);
        GenericValue yields = new GenericValue();
        yields.setBid(new BigDecimal(Math.random()*5).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        yields.setMid(new BigDecimal(Math.random()*5).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        yields.setAsk(new BigDecimal(Math.random()*5).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        genericPrice.setYield(yields);
        GenericSpread spread = new GenericSpread();
        spread.setBenchmarkId("BenchmarkId");
        spread.setBenchmarkType("XS"+new Long(100000000+Math.round(Math.random()*100000)).toString());
        GenericValue spreadValues = new GenericValue();
        spreadValues.setAsk(new BigDecimal(Math.random()).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        spreadValues.setMid(new BigDecimal(Math.random()).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        spreadValues.setBid(new BigDecimal(Math.random()).setScale(15,BigDecimal.ROUND_HALF_EVEN));
        spread.setGenericValue(spreadValues);
        genericPrice.setSpread(spread);
        return genericPrice;
    }


}
