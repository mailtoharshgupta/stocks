package com.exchange.stockmarket.services.web.impl;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.core.util.StopWatch;
import com.exchange.stockmarket.services.das.IStockDAS;
import com.exchange.stockmarket.services.web.IStartupDataLoader;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;


/**
 * @author Harsh Gupta on {2/10/18}
 * <p>
 * Used to generate sample data on application boot up.
 */
@Service("startupDataLoader")
public class StartupDataLoader implements IStartupDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupDataLoader.class);

    private final IStockDAS stockDAS;
    private final ResourceLoader resourceLoader;

    private final Integer minPriceBound = 10;
    private final Integer maxPriceBound = 1000;

    @Autowired
    public StartupDataLoader(IStockDAS stockDAS, ResourceLoader resourceLoader) {
        this.stockDAS = stockDAS;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void loadData() {
        LOGGER.debug("Loading startup data");
        StopWatch sw = new StopWatch();
        Random random = new Random();
        List<String> stockNames = Optional.ofNullable(getNamesList())
                .orElse(Arrays.asList("Apple","Intuit"));

        List<StockSRO> stockSROS = range(0, stockNames.size())
                .mapToObj(i -> StockSRO.builder()
                        .name(stockNames.get(i))
                        .price(getRandomPrice(random))
                        .build())
                .collect(Collectors.toList());

        stockSROS.forEach(stockDAS::saveOrUpdate);
        LOGGER.info("Loading startup data completed in {} ms", sw.elapsedTime(TimeUnit.MILLISECONDS));
    }

    private List<String> getNamesList() {
        //Loading data from name.yml file
        Map valuesMap = null;
        try {
            valuesMap = (Map) Yaml.load(resourceLoader.getResource("classpath:name.yml").getInputStream());
        } catch (IOException e) {
            LOGGER.error("Could not load startup data into memory ..");
            return null;
        }
        return (List<String>) valuesMap.get("names");
    }

    private Double getRandomPrice(Random random) {
        Integer price = random.nextInt(maxPriceBound - minPriceBound) + minPriceBound;
        return Double.valueOf(price);
    }

}
