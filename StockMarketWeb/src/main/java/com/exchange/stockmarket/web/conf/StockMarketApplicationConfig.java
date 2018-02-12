package com.exchange.stockmarket.web.conf;

import com.exchange.stockmarket.services.web.IStartupDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Harsh Gupta on {2/10/18}
 */

@Configuration
@EnableJpaAuditing
@EntityScan("com.exchange.stockmarket.core")
@EnableJpaRepositories("com.exchange.stockmarket.services")
@EnableTransactionManagement
public class StockMarketApplicationConfig {

    @Autowired
    private IStartupDataLoader startupDataLoader;

    @EventListener(ApplicationReadyEvent.class)
    public void loadStartUpData() {
        startupDataLoader.loadData();
    }

}
