package com.sethj.stocks.model;

import java.math.BigDecimal;

public class ClosingPrice {
    private String date;

    private BigDecimal closingPrice;

    public ClosingPrice(String date, BigDecimal closingPrice){
        this.date = date;
        this.closingPrice = closingPrice;
    }

    public String getDate(){
        return this.date;
    }

    public BigDecimal getClosingPrice(){
        return this.closingPrice;
    }
}
