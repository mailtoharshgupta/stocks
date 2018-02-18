package com.exchange.stockmarket.core.exception;


/**
 * @author Harsh Gupta on {2/10/18}
 */

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
