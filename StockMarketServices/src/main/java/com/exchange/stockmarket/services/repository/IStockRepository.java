package com.exchange.stockmarket.services.repository;

import com.exchange.stockmarket.core.entity.StockEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Harsh Gupta on {2/10/18}
 */
public interface IStockRepository extends PagingAndSortingRepository<StockEntity, Long> {
}
