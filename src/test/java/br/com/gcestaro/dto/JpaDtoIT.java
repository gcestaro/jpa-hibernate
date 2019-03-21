package br.com.gcestaro.dto;

import br.com.gcestaro.model.relationship.SalesOrder;
import br.com.gcestaro.test.util.JpaIT;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaDtoIT extends JpaIT {

    @Test
    public void findAsDtoMapping() {
        SalesOrder salesOrder = new SalesOrder();
        doPersist(salesOrder);
        doCommit();

        closeCurrentAndStartNewSession();

        String hql =
                "SELECT new br.com.gcestaro.dto.SalesOrderDTO(so.id, so.status) " +
                        "FROM SALES_ORDER so " +
                        "WHERE so.id = :id";

        TypedQuery<SalesOrderDTO> query = entityManager.createQuery(hql, SalesOrderDTO.class);
        query.setParameter("id", salesOrder.getId());

        SalesOrderDTO salesOrderDTOExpected = new SalesOrderDTO(salesOrder.getId(), salesOrder.getStatus());

        SalesOrderDTO result = query.getSingleResult();

        assertEquals(salesOrderDTOExpected, result);
    }
}
