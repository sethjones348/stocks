package com.sethj.stocks.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.sethj.stocks.controller.StocksResponse;
import com.sethj.stocks.model.ClosingPrice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StocksService {
    private final WebService webService;

    public StocksService(WebService webService) {
        this.webService = webService;
    }

    public ResponseEntity<StocksResponse> getStocksBySymbol(String symbol) {
        List<ClosingPrice> closingPrices = getClosingPricesForTheLast11Days(symbol);

        double averageClosingPrice = calculateAverage(closingPrices.stream()
                .filter(c -> c.getClosingPrice().doubleValue() > 0.0)
                .map(c -> c.getClosingPrice())
                .collect(Collectors.toList()));

        return new ResponseEntity<>(new StocksResponse(String.valueOf(averageClosingPrice), closingPrices),
                HttpStatus.OK);
    }

    private double calculateAverage(List<BigDecimal> numbers) {
        double sum = 0.0;

        for (BigDecimal number : numbers) {
            sum += number.doubleValue();
        }

        return sum / numbers.size();
    }

    private List<ClosingPrice> getClosingPricesForTheLast11Days(String symbol) {
        List<String> dateListOfLast11Days = getDateListLast11Days();

        List<ClosingPrice> results = new ArrayList<>();

        dateListOfLast11Days.forEach(date -> {
            ClosingPrice closingPrice = webService.fetchOpenCloseForDate(date, symbol);

            if (closingPrice.getClosingPrice() != null) {
                results.add(closingPrice);
                System.out.println(
                        "Date: " + closingPrice.getDate() + " Closing Price: " + closingPrice.getClosingPrice());
            }
        });

        return results;
    }

    private List<String> getDateListLast11Days() {
        LocalDate today = LocalDate.now();
        LocalDate elevenDaysAgo = today.minusDays(11);

        return elevenDaysAgo
                .datesUntil(today)
                .sorted(Comparator.reverseOrder())
                .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .collect(Collectors.toList());
    }
}