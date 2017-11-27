# avro-examples

Examples showing using a an existing Java File to generate a avro schema. It outputs a schema where all fields are nullable. 
the schema can be adapted to make it more stringent. It converts BigDecimals to avro DECIMAL with a scale of 15 and a precision of 30 
rounding half even it necessary.

Run 
``maven install``
to generate java classes based on the schema in 
``src/main/avro/genericPrice.avsc``

Main classes are
JavaToAvroSchema - this shows generating a schema off existing java classes and serialising and deserialising
AvroSerialisationFromGeneratedJava - this shows using the classes generated off the schema in the maven build to serialise and deserialise

Note this is a bug in the current bug in avro 1.8.2 (and below) that mean when using decimal types you need to write

```java
    SpecificData specificData = new SpecificData();
    specificData.addLogicalTypeConversion(new DecimalConversion());
    DatumReader<GenericPrice> reader = new SpecificDatumReader<GenericPrice>(new GenericPrice().getSchema(),new GenericPrice().getSchema(),specificData);
```

rather than just

```java
   DatumReader<GenericPrice> reader = new SpecificDatumReader<GenericPrice>(new GenericPrice().getSchema());
```

and 

```java
    SpecificData specificData = new SpecificData();
    specificData.addLogicalTypeConversion(new DecimalConversion());
    DatumWriter<GenericPrice> writer = new SpecificDatumWriter<GenericPrice>(new GenericPrice().getSchema(),specificData);
```

rather than just

```java
    DatumWriter<GenericPrice> writer = new SpecificDatumWriter<GenericPrice>(new GenericPrice().getSchema());
```

This is fixed under https://issues.apache.org/jira/browse/AVRO-1891 and due for release in 1.8.3 (current verison is 1.8.2)
