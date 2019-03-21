package br.com.gcestaro.dto;

import br.com.gcestaro.model.relationship.OrderStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class SalesOrderDTO implements Serializable {

    private Long id;

    private OrderStatus status;

    public SalesOrderDTO() {

    }

    public SalesOrderDTO(Long id, OrderStatus status) {
        this.id = id;
        this.status = status;
    }
}
