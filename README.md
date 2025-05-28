# Cafe POS System

A Java-based Point of Sale (POS) system for cafes using JavaFX and Hibernate.

## Features

- User Management (Admin/Staff roles)
- Menu Item Management
- Order Processing
- Database Integration with MySQL

## Technology Stack

- Java 21
- JavaFX 23 (GUI Framework)
- Hibernate 6.4.4 (ORM Framework)
- MySQL 8 (Database)
- Maven (Build Tool)

## Project Structure
```
cafe-pos/
├──src/
|  ├──main/
|  |  ├──java/
|  |  |  ├──module-info.java
|  |  |  └──com/
|  |  |     └──example/
|  |  |        ├──controller/
|  |  |        ├──dao/
|  |  |        ├──model/
|  |  |        ├──util/
|  |  |        └──App.java
|  |  └──resources/
|  |     └──hibernate.cfg.xml
|  |     └──com/
|  |        └──example/
|  |           ├──view/
|  |           └──image/
|  └──test/
|     └──java/
|        └──com/
|           └──example/
├──.vscode/
|  └──settings.json
├──target/
|     ...
├──LICENSE
├──pom.xml
└──README.md
```

## Database Schema

- `users` - Store user information and credentials
- `menu_items` - Cafe menu items and their details
- `orders` - Customer orders
- `order_items` - Individual items in each order

## Setup Instructions

1. Install MySQL 8.0 or higher
2. Create a database named `cafe_pos`
3. Configure database connection in `src/main/resources/hibernate.cfg.xml`
4. Run Maven install:
   ```bash
   mvn clean install
5. Run the application:
   ```bash
   mvn JavaFX:run

License
This project is licensed under the MIT License - see the LICENSE file for details.

This README provides a comprehensive overview of your cafe POS project, including its features, technology stack, structure, and setup instructions. Feel free to modify it according to your specific needs!
