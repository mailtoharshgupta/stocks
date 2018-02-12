package com.exchange.stockmarket.services.web;

import com.exchange.stockmarket.base.model.StockSRO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @author Harsh Gupta on {2/10/18}
 * <p>
 * Interface for stock service.
 * Any business logc should fall under this layer.
 */
public interface IStockService {

    /**
     * Function to return a slice of stocks as request in the page request
     *
     * @param pageRequest Request Object for getting paginated list of Stocks {@link Pageable}
     * @return A paginated list of StockSROs {@link  Page<StockSRO>}
     */
    Page<StockSRO> getAllStocks(Pageable pageRequest);

    /**
     * Function that Fetches the stock identified by the id from the data layer.
     *
     * @param id Request param to get stock by id {@link Long}
     * @return {@link StockSRO} identified by the id passed in the request
     */
    StockSRO getStockById(Long id);

    /**
     * Function to apply any business logic on the passed stock object and pass it on to data layer to persist it.
     *
     * @param stockSRO SRO to save {@link StockSRO}
     * @return Saved {@link StockSRO}
     */
    StockSRO saveStock(StockSRO stockSRO);

    /**
     * Function to apply any business logic on the passed stock object and pass it on to data layer to update it.
     *
     * @param stockSRO SRO to update {@link StockSRO}
     * @return Updated {@link StockSRO}
     */
    StockSRO updateStock(StockSRO stockSRO);
}
