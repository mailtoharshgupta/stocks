package com.exchange.stockmarket.services.web;

/**
 * @author Harsh Gupta on {2/10/18}
 */
public interface IStartupDataLoader {

    /**
     * Loads the data on startup. Callee should be bound to an event that kicks in
     * at application startup.
     */
    void loadData();

}
