import avro.schema
from avro.datafile import DataFileReader, DataFileWriter
from avro.io import DatumReader, DatumWriter

schema = avro.schema.Parse(open("D:/dev/ijprojects/avro-examples/genericPrice.avsc", "rb").read())

writer = DataFileWriter(open("python.avro", "wb"), DatumWriter(), schema)
writer.append({'id': 'Id', 'idType': 'ISIN', 'price': {'bid': 100.1, 'mid': 100.1, 'ask':100.1}, 'yield': {'bid': 0.01, 'mid': 0.01, 'ask': 0.001}, 'spread': {'benchmarkId': 'BenchmarkId', 'benchmarkType': 'ISIN', 'genericValue': {'bid': 0.001, 'mid': 0.001, 'ask': 0.001}}})
writer.close()

reader = DataFileReader(open("D:/dev/ijprojects/avro-examples/genericPrice.avro", "rb"), DatumReader())
for user in reader:
  print(user)
reader.close()