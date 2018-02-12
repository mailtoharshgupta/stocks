package com.exchange.stockmarket.services.das.impl;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.core.entity.StockEntity;
import com.exchange.stockmarket.core.util.StockMarketUtil;
import com.exchange.stockmarket.services.config.ServicesTestConfig;
import com.exchange.stockmarket.services.das.IStockDAS;
import com.exchange.stockmarket.services.repository.IStockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class StockDASImplTest {

    @MockBean
    private IStockRepository stockRepository;

    @Autowired
    private IStockDAS stockDAS;

    @Test
    public void getAll() throws Exception {
        List<StockEntity> stockEntities = createStock(Arrays.asList("Apple", "Intuit"));

        when(stockRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(stockEntities));
        PageRequest pageRequest = new PageRequest(0, 10);
        assertEquals(2, stockDAS.getAll(pageRequest).getContent().size());
    }

    @Test
    public void getAllWithNoRecords() throws Exception {
        when(stockRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        PageRequest pageRequest = new PageRequest(0, 10);
        assertEquals(0, stockDAS.getAll(pageRequest).getContent().size());
    }

    @Test
    public void getById() throws Exception {
        StockEntity entity = StockEntity.builder().id(1L).name("Apple").price(150.00).updated(StockMarketUtil.getCurrentTime()).build();
        when(stockRepository.findOne(1L)).thenReturn(entity);
        StockSRO sro = stockDAS.getById(1L);
        assertEquals(entity.getId(), sro.getId());
        assertEquals(entity.getPrice(), sro.getPrice());
    }

    @Test
    public void getByIdWhenDoesNotExist() throws Exception {
        when(stockRepository.findOne(1L)).thenReturn(null);
        StockSRO sro = stockDAS.getById(1L);
        assertEquals(null, sro);
    }


    public void saveOrUpdateStock() throws Exception {
        StockSRO requestStockSRO = StockSRO.builder().name("Apple").price(150.00).build();
        StockEntity savedEntity = StockEntity.builder().name("Apple").price(150.00).id(1L).updated(StockMarketUtil.getCurrentTime()).build();

        when(stockRepository.save(any(StockEntity.class))).thenReturn(savedEntity);

        StockSRO savedSRO = stockDAS.saveOrUpdate(requestStockSRO);
        assertEquals(savedEntity.getId(), savedSRO.getId());
        assertEquals(savedEntity.getPrice(), savedSRO.getPrice());
        assertEquals(savedEntity.getName(), savedSRO.getName());

    }

    private List<StockEntity> createStock(List<String> stockNames) {
        return stockNames.stream()
                .map(name -> StockEntity.builder().name(name).price(100.00).build())
                .collect(Collectors.toList());
    }


}