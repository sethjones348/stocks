package com.sethj.stocks.controller;

import java.util.List;

import com.sethj.stocks.model.ClosingPrice;

public class StocksResponse {
    String averagePrice;

    List<ClosingPrice> closingPrices;

    public StocksResponse(String averagePrice, List<ClosingPrice> closingPrices) {
        this.averagePrice = averagePrice;
        this.closingPrices = closingPrices;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public List<ClosingPrice> getClosingPrices() {
        return closingPrices;
    }

    public void setClosingPrices(List<ClosingPrice> closingPrices) {
        this.closingPrices = closingPrices;
    }
}