package com.coffeepoint.avro;

public class GenericPrice {

    private String id;
    private String idType;
    private GenericValue price;
    private GenericValue yield;
    private GenericSpread spread;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public GenericValue getPrice() {
        return price;
    }

    public void setPrice(GenericValue price) {
        this.price = price;
    }

    public GenericValue getYield() {
        return yield;
    }

    public void setYield(GenericValue yield) {
        this.yield = yield;
    }

    public GenericSpread getSpread() {
        return spread;
    }

    public void setSpread(GenericSpread spread) {
        this.spread = spread;
    }
}
