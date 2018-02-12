package com.exchange.stockmarket.services.das.impl;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.core.entity.StockEntity;
import com.exchange.stockmarket.core.util.StockMarketUtil;
import com.exchange.stockmarket.services.das.IStockDAS;
import com.exchange.stockmarket.services.repository.IStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Harsh Gupta on {2/10/18}
 */

@Transactional
@Service("stockDAS")
public class StockDASImpl implements IStockDAS {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockDASImpl.class);

    private IStockRepository stockRepository;


    @Autowired
    public StockDASImpl(IStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Page<StockSRO> getAll(Pageable pageRequest) {
        LOGGER.debug("Going to fetch stocks for the page number : {} and size : {}", pageRequest.getPageNumber(), pageRequest.getPageSize());
        Page<StockEntity> entities = stockRepository.findAll(pageRequest);
        LOGGER.debug("Stocks received from repository {} :", entities.getTotalElements());
        return entities.map(entry -> (StockSRO) StockMarketUtil.convert(entry, StockSRO.class));
    }

    @Override
    public StockSRO getById(Long id) {
        LOGGER.debug("Going to fetch stock for id : {} ", id);
        StockEntity entity = stockRepository.findOne(id);
        if (null == entity) {
            LOGGER.error("Couldn't fetch Stock by id : {}", id);
            return null;
        }
        return (StockSRO) StockMarketUtil.convert(entity, StockSRO.class);
    }

    @Override
    public StockSRO saveOrUpdate(StockSRO stockSRO) {
        LOGGER.debug("Going to save/update stock : {}", stockSRO);
        StockEntity entity = (StockEntity) StockMarketUtil.convert(stockSRO, StockEntity.class);
        StockEntity saved = stockRepository.save(entity);
        LOGGER.debug("Persisted stock : {}", saved);
        return (StockSRO) StockMarketUtil.convert(saved, StockSRO.class);
    }

}