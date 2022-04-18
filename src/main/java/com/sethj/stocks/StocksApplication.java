package com.sethj.stocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import controller.StockController;

@SpringBootApplication
@ComponentScan(basePackageClasses={StockController.class})
public class StocksApplication {	
	public static void main(String[] args) {
		SpringApplication.run(StocksApplication.class, args);
	}
}
