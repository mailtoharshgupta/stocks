package com.exchange.stockmarket.base.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @author Harsh Gupta on {2/10/18}
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvalidParamException extends RuntimeException {

    public InvalidParamException(String message) {
        super(message);
    }

}
