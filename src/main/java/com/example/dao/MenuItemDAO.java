package com.example.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.model.MenuItem;

public class MenuItemDAO extends GenericDAO<MenuItem> {

    public MenuItemDAO() {
        super(MenuItem.class);
    }

    public List<MenuItem> findByCategory(String category) {
        try (Session session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM MenuItem WHERE category = :category", MenuItem.class)
                .setParameter("category", category)
                .list();
        }
    }

    public List<MenuItem> findByAvailability(boolean isAvailable) {
        try (Session session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM MenuItem WHERE available = :isAvailable", MenuItem.class)
                .setParameter("isAvailable", isAvailable)
                .list();
        }
    }

    public List<MenuItem> findByCategoryAndAvailability(String category, boolean isAvailable) {
        try (Session session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM MenuItem WHERE category = :category AND available = :isAvailable", MenuItem.class)
                .setParameter("category", category)
                .setParameter("isAvailable", isAvailable)
                .list();
        }
    }

    public List<MenuItem> searchByName(String name, String category, boolean isAvailable) {
        try (Session session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM MenuItem WHERE LOWER(name) LIKE :name";
            if (category != null && !category.equals("All")) {
                hql += " AND category = :category";
            }
            if (isAvailable) {
                hql += " AND available = true";
            }else {
                hql += " AND available = false";
            }
            var query = session.createQuery(hql, MenuItem.class);
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            if (category != null && !category.equals("All")) {
                query.setParameter("category", category);
            }
            return query.list();
        }catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public int countItemByCategory(String category) {
        try (Session session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return ((Long) session.createQuery(
                    "SELECT COUNT(*) FROM MenuItem WHERE category = :category")
                .setParameter("category", category)
                .uniqueResult()).intValue();
        }
    }
    public int getAllItemCount() {
        try (Session session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return ((Long) session.createQuery("SELECT COUNT(*) FROM MenuItem")
                .uniqueResult()).intValue();
        }
    }
}
