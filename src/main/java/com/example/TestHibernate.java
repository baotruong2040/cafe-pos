package com.example;

import org.hibernate.Session;

import com.example.util.HibernateUtil;


public class TestHibernate {
    public static void main(String[] args) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            System.out.println("Hibernate conect sucessfully");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
