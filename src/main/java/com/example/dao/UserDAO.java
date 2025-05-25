package com.example.dao;

import com.example.model.User;

public class UserDAO extends GenericDAO<User> {
    public UserDAO() {
        super(User.class);
    }

    public User findByUsername(String username) {
        try (var session = com.example.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    public boolean isValidUser(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public String getUserRole(String username) {
        User user = findByUsername(username);
        if (user != null) {
            return user.getRole().name();
        } else {
            System.out.println("User not found");
        }
        return null;
    }
}
