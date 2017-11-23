package com.coffeepoint.avro;

import java.math.BigDecimal;

public class GenericValue {

    private BigDecimal bid;
    private BigDecimal mid;
    private BigDecimal ask;

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getMid() {
        return mid;
    }

    public void setMid(BigDecimal mid) {
        this.mid = mid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    @Override
    public String toString() {
        return "GenericValue{" +
            "bid=" + bid +
            ", mid=" + mid +
            ", ask=" + ask +
            '}';
    }
}
