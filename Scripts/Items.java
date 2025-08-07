package vending_M_F;

import java.sql.Date;

/**
 * The {@code Items} class represents a product or user in the vending machine system.
 * It contains fields and methods for both product details (ID, name, price, stock, expiry date)
 * and user credentials (username, password, role).
 */
public class Items {

    // Product-related fields
    private int id;             // Unique product ID
    private int price;          // Price of the product
    private int stock;          // Quantity in stock
    private String name;        // Product name
    private Date expiryDate;    // Product expiration date

    // User-related fields
    private String username;    // Login username
    private String password;    // Login password
    private String role;        // User role (optional - e.g., admin, staff)

    // ===== USER GETTERS & SETTERS =====

    /**
     * Gets the username of the user.
     * @return username as String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     * @param username the login username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     * @return password as String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     * @param password the login password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    // ===== PRODUCT GETTERS & SETTERS =====

    /**
     * Gets the product ID.
     * @return product ID as int
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the product ID.
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the product price.
     * @return price as int
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the product price.
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Gets the available stock quantity.
     * @return stock as int
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock quantity.
     * @param stock the quantity to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the product name.
     * @return name as String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the expiry date of the product.
     * @return expiry date as java.sql.Date
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date of the product.
     * @param expiryDate the expiry date to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    // ===== OPTIONAL: User role =====

    /**
     * Gets the role of the user.
     * @return role as String
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     * @param role the role to set (e.g., "admin", "staff")
     */
    public void setRole(String role) {
        this.role = role;
    }
}
