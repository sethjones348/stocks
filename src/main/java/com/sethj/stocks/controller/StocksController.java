package com.sethj.stocks.controller;

import com.sethj.stocks.service.StocksService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StocksController {

    private StocksService stocksService;

    public StocksController(StocksService stocksService) {
        this.stocksService = stocksService;
    }

    @GetMapping("/stocks/{symbol}")
    public ResponseEntity<StocksResponse> getStocksBySymbol(@PathVariable String symbol) {
        return stocksService.getStocksBySymbol(symbol);
    }
}
