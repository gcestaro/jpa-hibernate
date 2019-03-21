package br.com.gcestaro.model.relationship;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ORDER_ITEM")
public class OrderItem implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    private SalesOrder salesOrder;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    private Product product;
}
