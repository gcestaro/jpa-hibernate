package br.com.gcestaro.model.relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PRODUCTION_ORDER")
public class ProductionOrder implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private SalesOrder salesOrder;

    public ProductionOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }
}
