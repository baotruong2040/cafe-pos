package com.example.dao;

import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.util.HibernateUtil;

import javafx.application.Platform;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

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

    public List<Object[]> getWeeklyRevenue() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            
            LocalDate sixDaysAgo = LocalDate.now().minusDays(6);

            List<Object[]> result = session.createQuery("""
                    SELECT FUNCTION('DATE', o.orderTime), SUM(o.totalAmount)
                    FROM Order o
                    WHERE o.orderTime >= :sixDaysAgo
                    GROUP BY FUNCTION('DATE', o.orderTime)
                    ORDER BY FUNCTION('DATE', o.orderTime)
                    """, Object[].class)
                    .setParameter("sixDaysAgo", sixDaysAgo.atStartOfDay()) // datetime
                    .list();

            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public double getDailyRevenue() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE DATE(o.orderTime) = CURRENT_DATE", Double.class)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getDailyOrderCount() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COUNT(o) FROM Order o WHERE DATE(o.orderTime) = CURRENT_DATE", Long.class)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getWeeklyOrderCount() {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            LocalDate sixDaysAgo = LocalDate.now().minusDays(6);
        
            Long totalOrders = session.createQuery("""
            SELECT COUNT(o)
            FROM Order o
            WHERE o.orderTime >= :sixDaysAgo
            """, Long.class)
            .setParameter("sixDaysAgo", sixDaysAgo.atStartOfDay())
            .uniqueResult();
            return totalOrders;
        }catch (Exception e ) {
            return 0;
        }
    }

    public Order saveOrder(Order order) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(order);
            session.flush();
            
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
                session.save(item);
            }

            transaction.commit();
            return order;
        } catch (Exception e) {
           if (transaction != null) {
                transaction.rollback();
            }
            Platform.runLater(() -> {
                e.printStackTrace();
            });
            return null;
        }
    }
}
