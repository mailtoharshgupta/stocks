package com.exchange.stockmarket.services.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Harsh Gupta on {2/11/18}
 */
@TestConfiguration
@EnableJpaAuditing
@EntityScan("com.exchange.stockmarket.core")
@EnableJpaRepositories("com.exchange.stockmarket.services")
@EnableTransactionManagement
@ComponentScan("com.exchange.stockmarket.services")
@EnableAutoConfiguration
public class ServicesTestConfig {
}
