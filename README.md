# Vending Machine Application

A Java Swing-based desktop application simulating a vending machine with product selection, purchase processing, and database integration.

---

## Features

- Display a list of available products with details (name, price, stock).
- User-friendly GUI with a modern FlatLightLaf theme.
- Allows users to insert money and buy products.
- Validates user input and handles insufficient funds.
- Tracks product stock and updates it after each purchase.
- Warns users if the product expiry date is within 6 months.
- Records sales data with timestamps into a MySQL database.
- Navigation between screens with a back button.

---

## Technologies Used

- Java SE (Swing for GUI)
- MySQL (Database for products and sales)
- FlatLaf (Look and Feel library for modern UI)
- JDBC (Database connectivity)

---

## Project Structure

- `Machine.java` — Main window for product selection and purchasing.
- `Items.java` — Model class representing a product/item.
- `first.java` — (Assumed) Entry point / login or welcome screen.
- MySQL database with two tables:
  - `products` (id, name, price, stock, expiry date)
  - `sales` (product_id, quantity, date)

---

## Setup Instructions

### Prerequisites

- Java Development Kit (JDK 8 or higher)
- MySQL server installed and running
- IDE or build tool (e.g., IntelliJ IDEA, Eclipse)

### Database Setup

1. Create a database named `vending_machine`.
2. Create the `products` and `sales` tables. Example schema:

```sql
CREATE TABLE products (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  price DECIMAL(10, 2),
  stock INT,
  expiry_date DATE
);

CREATE TABLE sales (
  id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT,
  quantity INT,
  date TIMESTAMP,
  FOREIGN KEY (product_id) REFERENCES products(id)
);
