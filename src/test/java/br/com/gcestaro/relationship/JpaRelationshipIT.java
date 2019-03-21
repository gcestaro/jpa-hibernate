package br.com.gcestaro.relationship;

import br.com.gcestaro.model.relationship.OrderItem;
import br.com.gcestaro.model.relationship.Product;
import br.com.gcestaro.model.relationship.SalesOrder;
import br.com.gcestaro.test.util.JpaIT;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class JpaRelationshipIT extends JpaIT {

    @Test
    public void relationshipsPersistence() {
        Product product = Product.builder()
                .description("Product X")
                .salePrice(new BigDecimal("2.99"))
                .build();

        doPersist(product);

        SalesOrder salesOrder = new SalesOrder();

        doPersist(salesOrder);

        OrderItem item = OrderItem.builder()
                .amount(new BigDecimal("5"))
                .product(product)
                .salesOrder(salesOrder)
                .build();

        doPersist(item);

        salesOrder.addOrderItem(item);
        salesOrder.request();

        doMerge(salesOrder);

        doCommit();
        closeCurrentAndStartNewSession();
    }
}
