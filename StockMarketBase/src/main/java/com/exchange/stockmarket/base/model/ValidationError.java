package com.exchange.stockmarket.base.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

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
public class ValidationError implements Serializable {

    private static final long serialVersionUID = 760859189756122L;

    private String field;

    private String message;

}