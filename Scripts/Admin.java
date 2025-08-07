package vending_M_F;

// Import necessary libraries
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Admin login GUI for the vending machine system.
 * Handles user authentication using credentials stored in a MySQL database.
 */
public class Admin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * Constructor to initialize the admin login window.
     */
    public Admin() {
        setTitle("Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null); // Center the frame on screen

        // Main panel with modern UI
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Header title
        JLabel headerLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(70, 70, 70));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form section for username and password
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setOpaque(false);

        // Username input panel
        JPanel usernamePanel = new JPanel(new BorderLayout(5, 2));
        usernamePanel.setOpaque(false);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField = new JTextField();
        styleTextField(usernameField);
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        // Password input panel
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 2));
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        // Add username and password panels to form
        formPanel.add(usernamePanel);
        formPanel.add(passwordPanel);

        // Login button setup
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(0, 120, 215)); // Blue
        loginButton.addActionListener(this::performLogin);
        formPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel);
    }

    /**
     * Applies styling to text fields for a consistent UI.
     */
    private void styleTextField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setBackground(Color.WHITE
