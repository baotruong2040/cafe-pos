package com.example.dao;

import com.example.model.OrderItem;
import com.example.util.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<Object[]> getWeeklyRevenueByCategory() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        LocalDate sixDaysAgo = LocalDate.now().minusDays(6);
        LocalDateTime startTime = sixDaysAgo.atStartOfDay();
        
        return session.createQuery("""
            SELECT mi.category, 
                   SUM(oi.quantity) as totalQuantity,
                   SUM(oi.price * oi.quantity) as totalRevenue,
                   COUNT(DISTINCT oi.order.id) as orderCount
            FROM OrderItem oi
            JOIN oi.menuItem mi
            JOIN oi.order o
            WHERE o.orderTime >= :startTime
            GROUP BY mi.category
            ORDER BY totalRevenue DESC
            """, Object[].class)
            .setParameter("startTime", startTime)
            .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
