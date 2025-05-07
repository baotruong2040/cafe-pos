package com.example.dao;

import java.util.List;

import com.example.model.MenuItem;

public class MenuItemDAO extends GenericDAO<MenuItem> {

    public MenuItemDAO() {
        super(MenuItem.class);
    }

    // Thêm phương thức tùy chỉnh nếu cần
    public List<MenuItem> findByCategory(String category) {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM MenuItem WHERE category = :category", MenuItem.class)
                .setParameter("category", category)
                .list();
        }
    }
}
