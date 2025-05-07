package com.example.dao;

import com.example.model.Order;

import java.util.List;

public class OrderDAO extends GenericDAO<Order> {

    public OrderDAO() {
        super(Order.class);
    }

    public List<Order> findByUserId(int userId) {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order WHERE user.id = :userId", Order.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public List<Order> findTodayOrders() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order WHERE DATE(orderTime) = CURRENT_DATE", Order.class)
                    .list();
        }
    }
}
