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

    // Open packages for JavaFX and Hibernate
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.model to org.hibernate.orm.core;

    // Export the main package
    exports com.example;
}
