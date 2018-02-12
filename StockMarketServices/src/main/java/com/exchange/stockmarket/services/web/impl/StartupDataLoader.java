package com.exchange.stockmarket.services.web.impl;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.core.util.StopWatch;
import com.exchange.stockmarket.services.das.IStockDAS;
import com.exchange.stockmarket.services.web.IStartupDataLoader;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
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

    private final Integer minPriceBound = 10;
    private final Integer maxPriceBound = 1000;

    @Autowired
    public StartupDataLoader(IStockDAS stockDAS) {
        this.stockDAS = stockDAS;
    }

    @Override
    public void loadData() {
        LOGGER.debug("Loading startup data");
        StopWatch sw = new StopWatch();
        Random random = new Random();
        List<String> stockNames = getNamesList();
        List<StockSRO> stockSROS = range(1, 200)
                .mapToObj(i -> StockSRO.builder()
                        .name(stockNames.get(i))
                        .price(getRandomPrice(random))
                        .build())
                .collect(Collectors.toList());

        stockSROS.forEach(stockDAS::saveOrUpdate);
        LOGGER.info("Loading startup data completed in {} ms", sw.elapsedTime(TimeUnit.MILLISECONDS));
    }

    private List<String> getNamesList() {
        //Loading data from names.yml file
        Map valuesMap = (Map) Yaml.load(ClassLoader.getSystemResourceAsStream("name.yml"));
        return (List<String>) valuesMap.get("names");
    }

    private Double getRandomPrice(Random random) {
        Integer price = random.nextInt(maxPriceBound - minPriceBound) + minPriceBound;
        return Double.valueOf(price);
    }

}
