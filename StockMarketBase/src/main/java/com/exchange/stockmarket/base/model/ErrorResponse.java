package com.exchange.stockmarket.base.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Harsh Gupta on {2/10/18}
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 760859189756122L;

    private int status;
    private String message;
    private List<ValidationError> validationErrors;

}
