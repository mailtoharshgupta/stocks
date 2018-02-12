package com.exchange.stockmarket.web.controller;

import com.exchange.stockmarket.base.exception.InvalidParamException;
import com.exchange.stockmarket.base.exception.ResourceNotFoundException;
import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.services.web.IStockService;
import com.exchange.stockmarket.web.annotation.Document;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

/**
 * @author Harsh Gupta on {2/10/18}
 */

@RestController
@Document
@RequestMapping(StockMarketWebServiceController.URL)
public class StockMarketWebServiceController {

    protected static final String URL = "/api/stocks";

    private static final Logger LOGGER = LoggerFactory.getLogger(StockMarketWebServiceController.class);

    private final IStockService stockService;

    @Autowired
    public StockMarketWebServiceController(IStockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Get API for getting paginated list of stocks
     *
     * @param pageNumber
     * @param size
     * @return Paginated list of stocks {@link Iterable<StockSRO>}
     */
    @ApiOperation(value = "Gets the paginated list of stocks ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Paginated List of stocks is successfully fetched"),
            @ApiResponse(code = 400, message = "Invalid request params passed."),
            @ApiResponse(code = 404, message = "Resource not found")
    })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<StockSRO>> getAll(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                     @RequestParam(name = "size", defaultValue = "20") int size) {

        LOGGER.debug("Request received for getting all stocks with page number : {}, and page size : {}", pageNumber, size);
        if (size < 1) {
            throw new InvalidParamException("Page size must not be less than 1");
        }
        Pageable pageRequest = new PageRequest(pageNumber, size);
        Iterable<StockSRO> stocks = stockService.getAllStocks(pageRequest);
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }


    /**
     * Get API for getting a stock by ID
     *
     * @param id
     * @return A {@link StockSRO} identified by ID
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched the stock identified by passed ID successfully"),
            @ApiResponse(code = 404, message = "Stock identified by passed ID does not exist")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockSRO> getById(@PathVariable Long id) {
        LOGGER.debug("Request received for getting stock with Id {}", id);
        StockSRO stock = Optional.ofNullable(stockService.getStockById(id))
                .orElseThrow(
                        () -> {
                            LOGGER.debug("Stock with Id : {} not found", id);
                            return new ResourceNotFoundException("Stock with ID " + id + " not found");
                        });
        return ResponseEntity.ok(stock);
    }

    /**
     * Post API to create a new stock
     *
     * @param stock
     * @return created {@link StockSRO}
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a new stock resource successfully"),
            @ApiResponse(code = 404, message = "Resource Not found"),
            @ApiResponse(code = 400, message = "Bad Request params")
    })
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockSRO> createStock(@Validated(StockSRO.New.class) @RequestBody StockSRO stock, UriComponentsBuilder uriBuilder) {

        LOGGER.debug("Request received for creating stock {}", stock);
        StockSRO persistedStock = stockService.saveStock(stock);
        URI getByIdURI = uriBuilder.path("/api/user/{id}").buildAndExpand(persistedStock.getId()).toUri();
        return ResponseEntity.created(getByIdURI).body(persistedStock);

    }

    /**
     * Put API to update the price of a stock identified by ID
     *
     * @param stock
     * @param id
     * @return Updated {@link StockSRO}
     */
    @ApiOperation(value = "Updates the price of the stock")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated the price of the stock."),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 400, message = "Bad Request params")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockSRO> updatePrice(@Validated(StockSRO.Update.class) @RequestBody StockSRO stock,
                                                @PathVariable Long id, UriComponentsBuilder uriBuilder) {

        LOGGER.debug("Request received to update price to : {} of stock id : {}", stock.getPrice(), id);

        StockSRO persistedStock = Optional.ofNullable(stockService.getStockById(id))
                .orElseThrow(
                        () -> {
                            LOGGER.debug("Stock with Id : {} not found", id);
                            return new ResourceNotFoundException("Stock with ID " + id + " not found");
                        });

        persistedStock.setPrice(stock.getPrice());
        StockSRO updatedStock = stockService.updateStock(persistedStock);
        return ResponseEntity.ok().body(updatedStock);
    }

}