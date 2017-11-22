# avro-examples

Examples showing using a an existing Java File to generate a avro schema. It outputs a schema where all fields are nullable. 
the schema can be adapted to make it more stringent. It converts BigDecimals to avro DECIMAL with a scale of 15 and a precision of 30 
rounding half even it necessary. 

Main class is  JavaToAvroSchema

