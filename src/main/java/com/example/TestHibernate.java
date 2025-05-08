package com.example;

import com.example.model.MenuItem;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestHibernate {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Tạo dữ liệu mẫu
            MenuItem item = new MenuItem();
            item.setName("Expresso");
            item.setPrice(30000);
            item.setCategory("Cà phê");
            item.setDescription("Cà phê thơm ngol");
            item.setImagePath(null);
            item.setAvailable(true);

            session.save(item);
            tx.commit();

            System.out.println("✅ Đã lưu thành công: " + item.getName());
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi kết nối Hibernate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
