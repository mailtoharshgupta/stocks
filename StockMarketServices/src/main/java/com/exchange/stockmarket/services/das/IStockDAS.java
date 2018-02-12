package com.exchange.stockmarket.services.das;

import com.exchange.stockmarket.base.model.StockSRO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @author Harsh Gupta on {2/10/18}
 * <p>
 * Interface for Stock's Data Access Service.
 * This is the only layer that the application needs to talk to.
 * Top layer need not worry about getting the data from different sources, or converting into it's own consumable object.
 * DAS layer does the logic of finding the data and converting it from different formats to a SRO.
 * In case we decide to move to another persistance db, or multilayer fetch, Callees of these
 * interface will not have to make any changes. Only the DAS layer will change.
 */
public interface IStockDAS {

    /**
     * Function to return a list of stocks as request in the page request
     *
     * @param pageRequest
     * @return A paginated list of  {@link Page<StockSRO>}
     */
    Page<StockSRO> getAll(Pageable pageRequest);


    /**
     * Function that returns the StockSRO from the persistence layer identified by the ID passed in the request.
     *
     * @param id
     * @return {@link StockSRO} identified by the id passed in the request
     */
    StockSRO getById(Long id);

    /**
     * Function to persist/update a Stock in the storage DB.
     *
     * @param stockSRO
     * @return Saved {@link StockSRO}
     */
    StockSRO saveOrUpdate(StockSRO stockSRO);

}
