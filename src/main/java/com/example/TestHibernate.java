package com.example;

import java.util.List;

import com.example.dao.MenuItemDAO;
import com.example.model.MenuItem;


public class TestHibernate {
    public static void main(String[] args) {
        MenuItemDAO menuItemDAO = new MenuItemDAO();
        List<MenuItem> items = menuItemDAO.findAll();

        for (MenuItem menuItem : items) {
            System.out.println(menuItem.getName());
            
        }
    }
}
