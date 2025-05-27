package com.example.dao;

import com.example.model.OrderItem;
import com.example.util.HibernateUtil;

import java.util.List;

import org.hibernate.Session;

public class OrderItemDAO extends GenericDAO<OrderItem> {

    public OrderItemDAO() {
        super(OrderItem.class);
    }

    public List<OrderItem> findByOrderId(int orderId) {
        try (Session session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM OrderItem WHERE order.id = :orderId", OrderItem.class)
                    .setParameter("orderId", orderId)
                    .list();
        }
    }
    
    public List<Object[]> findTop3MenuItems() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT oi.menuItem
                FROM OrderItem oi
                GROUP BY oi.menuItem.id
                ORDER BY SUM(oi.quantity) DESC
            """, Object[].class)
            .setMaxResults(3)
            .list();
        }
    }
}
