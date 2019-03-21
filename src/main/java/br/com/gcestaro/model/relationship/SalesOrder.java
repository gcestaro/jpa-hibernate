package br.com.gcestaro.model.relationship;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "SALES_ORDER")
public class SalesOrder implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "salesOrder", orphanRemoval = true)
    private List<OrderItem> items;

    @Getter
    @OneToOne(mappedBy = "salesOrder", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private ProductionOrder productionOrder;

    public SalesOrder() {
        status = OrderStatus.OPEN;
        items = new ArrayList<>();
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addOrderItem(OrderItem item) {
        items.add(item);
    }

    public void request() {
        if (items.isEmpty()) {
            throw new IllegalStateException(String.format("Request to SalesOrder %d failed due to no items have been added", id));
        }

        if (status.isRequested()) {
            throw new IllegalStateException(String.format("Request to SalesOrder %d failed due to already requested", id));
        }

        status = OrderStatus.REQUESTED;

        productionOrder = new ProductionOrder(this);
    }
}
