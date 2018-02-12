package com.exchange.stockmarket.base.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

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
@Builder
public class StockSRO implements Serializable {

    private static final long serialVersionUID = 760859189756122L;

    @ApiModelProperty(value = "Stock's Id")
    @Null(groups = {New.class, Update.class}, message = "Id has to be null")
    private Long id;

    @ApiModelProperty(value = "Stock's Name", required = true)
    @NotNull(groups = {Update.class, New.class}, message = "Stock name is required")
    private String name;

    @ApiModelProperty(value = "Stock's Price", required = true)
    @NotNull(groups = {Update.class, New.class}, message = "Stock's price can not be null")
    @Min(value = 0, message = "Stock's price can not be negative.")
    private Double price;

    @ApiModelProperty(value = "Stock's Last updated time")
    @Null(groups = {Update.class, New.class}, message = "Last updated time is auto calculated by the server.")
    private Date updated;

    public interface Update {
    }

    public interface New {
    }


}
