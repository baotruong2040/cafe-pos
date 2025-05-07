package com.example.dao;

import com.example.model.OrderItem;

import java.util.List;

public class OrderItemDAO extends GenericDAO<OrderItem> {

    public OrderItemDAO() {
        super(OrderItem.class);
    }

    public List<OrderItem> findByOrderId(int orderId) {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM OrderItem WHERE order.id = :orderId", OrderItem.class)
                    .setParameter("orderId", orderId)
                    .list();
        }
    }
}
