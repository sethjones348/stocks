package com.sethj.stocks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

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
	void test() {
		//Do nothing=
	}
}
