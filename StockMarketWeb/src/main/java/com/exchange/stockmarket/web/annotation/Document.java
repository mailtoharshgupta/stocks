package com.exchange.stockmarket.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Harsh Gupta on {2/10/18}
 * <p>
 * Used to identify APIs to be documented on Swagger
 */

@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface Document {
}
