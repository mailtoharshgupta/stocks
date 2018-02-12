package com.exchange.stockmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.exchange.stockmarket"})
public class StockMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockMarketApplication.class, args);
    }
}
