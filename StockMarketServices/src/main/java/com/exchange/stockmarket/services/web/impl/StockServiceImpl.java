package com.exchange.stockmarket.services.web.impl;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.services.das.IStockDAS;
import com.exchange.stockmarket.services.web.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * @author Harsh Gupta on {2/10/18}
 */

@Service("stockService")
public class StockServiceImpl implements IStockService {

    private final IStockDAS stockDAS;

    @Autowired
    public StockServiceImpl(IStockDAS stockDAS) {
        this.stockDAS = stockDAS;
    }

    @Override
    public Page<StockSRO> getAllStocks(Pageable pageRequest) {
        return stockDAS.getAll(pageRequest);
    }

    @Override
    public StockSRO getStockById(Long id) {
        return stockDAS.getById(id);
    }

    @Override
    public StockSRO saveStock(StockSRO stockSRO) {
        return stockDAS.saveOrUpdate(stockSRO);
    }

    @Override
    public StockSRO updateStock(StockSRO stockSRO) {
        return stockDAS.saveOrUpdate(stockSRO);
    }
}
