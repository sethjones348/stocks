package com.sethj.stocks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.sethj.stocks.controller.StocksResponse;
import com.sethj.stocks.model.ClosingPrice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class StocksServiceTest {

    @Mock
    private WebService webService;

    private StocksService stocksService;

    @BeforeEach
    void init(){
        webService = Mockito.mock(WebService.class);
        stocksService = new StocksService(webService);
    }
    
	@Test
	void getStocksBySymbol_happyPath() {
        ClosingPrice closingPrice = new ClosingPrice("2022-04-18", BigDecimal.TEN);
        when(webService.fetchOpenCloseForDate(any(String.class), any(String.class))).thenReturn(closingPrice);

        ResponseEntity<StocksResponse> result = stocksService.getStocksBySymbol("AAPL");

        assertThat(result.getBody().getAveragePrice()).isEqualTo("10.0");
        assertThat(result.getBody().getClosingPrices()).hasSize(11);
	}
}
