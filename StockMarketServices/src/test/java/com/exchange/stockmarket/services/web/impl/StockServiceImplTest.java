package com.exchange.stockmarket.services.web.impl;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.core.util.StockMarketUtil;
import com.exchange.stockmarket.services.config.ServicesTestConfig;
import com.exchange.stockmarket.services.das.IStockDAS;
import com.exchange.stockmarket.services.web.IStockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Harsh Gupta on {2/11/18}
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServicesTestConfig.class)
public class StockServiceImplTest {

    @MockBean
    private IStockDAS stockDAS;

    @Autowired
    private IStockService stockService;

    @Test
    public void getAll() throws Exception {
        List<StockSRO> stockSROS = createStock(Arrays.asList("Apple", "Intuit"));
        Page<StockSRO> page = new PageImpl<>(stockSROS);
        when(stockDAS.getAll(any(Pageable.class))).thenReturn(page);
        PageRequest pageRequest = new PageRequest(0, 10);
        assertEquals(2, stockService.getAllStocks(pageRequest).getContent().size());
    }

    @Test
    public void getAllWithNoRecords() throws Exception {
        when(stockDAS.getAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        PageRequest pageRequest = new PageRequest(0, 10);
        assertEquals(0, stockService.getAllStocks(pageRequest).getContent().size());
    }


    @Test
    public void getById() throws Exception {
        StockSRO sro = StockSRO.builder().id(1L).name("Apple").price(150.00).updated(StockMarketUtil.getCurrentTime()).build();
        when(stockDAS.getById(1L)).thenReturn(sro);
        StockSRO fromDB = stockService.getStockById(1L);
        assertEquals(sro.getId(), fromDB.getId());
        assertEquals(sro.getPrice(), fromDB.getPrice());
    }

    @Test
    public void getByIdWhenDoesNotExist() throws Exception {
        when(stockDAS.getById(1L)).thenReturn(null);
        StockSRO sro = stockService.getStockById(1L);
        assertEquals(null, sro);
    }

    @Test
    public void saveStock() throws Exception {
        StockSRO requestStockSRO = StockSRO.builder().name("Apple").price(150.00).build();
        StockSRO savedSRO = StockSRO.builder().name("Apple").price(150.00).id(1L).updated(StockMarketUtil.getCurrentTime()).build();

        when(stockDAS.saveOrUpdate(any(StockSRO.class))).thenReturn(savedSRO);

        StockSRO fromDB = stockService.saveStock(requestStockSRO);
        assertEquals(savedSRO.getId(), fromDB.getId());
        assertEquals(savedSRO.getPrice(), fromDB.getPrice());
        assertEquals(savedSRO.getName(), fromDB.getName());
    }


    @Test
    public void updateStock() throws Exception {
        StockSRO requestStockSRO = StockSRO.builder().name("Apple").price(150.00).build();
        StockSRO savedSRO = StockSRO.builder().name("Apple").price(150.00).id(1L).updated(StockMarketUtil.getCurrentTime()).build();

        when(stockDAS.saveOrUpdate(any(StockSRO.class))).thenReturn(savedSRO);

        StockSRO fromDB = stockService.updateStock(requestStockSRO);
        assertEquals(savedSRO.getId(), fromDB.getId());
        assertEquals(savedSRO.getPrice(), fromDB.getPrice());
        assertEquals(savedSRO.getName(), fromDB.getName());

    }


    private List<StockSRO> createStock(List<String> stockNames) {
        return stockNames.stream()
                .map(name -> StockSRO.builder().name(name).price(100.00).build())
                .collect(Collectors.toList());
    }

}