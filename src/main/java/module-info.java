module CoffePOS {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    // Hibernate + JPA
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // JDBC + MySQL
    requires java.sql;

    // Cho phép mở package để JavaFX và Hibernate sử dụng
    opens com.example to javafx.fxml, org.hibernate.orm.core;
    opens com.example.model to org.hibernate.orm.core;
    exports com.example;
}
