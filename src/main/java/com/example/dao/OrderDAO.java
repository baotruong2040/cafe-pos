package com.example.dao;

import com.example.model.Order;

import java.util.List;

public class OrderDAO extends GenericDAO<Order> {

    public OrderDAO() {
        super(Order.class);
    }

    public List<Order> findByUserId(int userId) {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order WHERE user.id = :userId", Order.class).setParameter("userId", userId).list();
        }
    }

    public List<Order> findTodayOrders() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order WHERE DATE(orderTime) = CURRENT_DATE", Order.class).list();
        }
    }

    public double getWeeklyRevenue() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.orderTime >= CURRENT_DATE - 7", Double.class)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getDailyRevenue() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE DATE(o.orderTime) = CURRENT_DATE", Double.class)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
