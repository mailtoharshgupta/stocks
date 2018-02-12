package com.exchange.stockmarket.core.entity;

import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


/**
 * @author Harsh Gupta on {2/10/18}
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@EntityListeners(AuditingEntityListener.class)
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Double price;

    @LastModifiedDate
    private Date updated;

}
