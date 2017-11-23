package com.coffeepoint.avro;

public class GenericSpread {

    private String benchmarkId;
    private String benchmarkType;
    private GenericValue genericValue;

    public String getBenchmarkId() {
        return benchmarkId;
    }

    public void setBenchmarkId(String benchmarkId) {
        this.benchmarkId = benchmarkId;
    }

    public String getBenchmarkType() {
        return benchmarkType;
    }

    public void setBenchmarkType(String benchmarkType) {
        this.benchmarkType = benchmarkType;
    }

    public GenericValue getGenericValue() {
        return genericValue;
    }

    public void setGenericValue(GenericValue genericValue) {
        this.genericValue = genericValue;
    }

    @Override
    public String toString() {
        return "GenericSpread{" +
            "benchmarkId='" + benchmarkId + '\'' +
            ", benchmarkType='" + benchmarkType + '\'' +
            ", genericValue=" + genericValue +
            '}';
    }
}
