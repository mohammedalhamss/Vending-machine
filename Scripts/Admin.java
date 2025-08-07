package vending_M_F;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Admin login screen for the vending machine system.
 * Authenticates admin credentials using a MySQL database.
 */
public class Admin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    /**
     * Constructor initializes UI and loads DB config.
     */
    public Admin() {
        loadDBConfig();
        initUI();
    }

    /**
     * Loads DB configuration from a properties file.
     */
    private void loadDBConfig() {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties props = new Properties();
            props.load(fis);
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPass = props.getProperty("db.password");
        } catch (IOException e) {
            showError("Failed to load database configuration: " + e.getMessage());
        }
    }

    /**
     * Initializes the Swing UI for the admin login screen.
     */
    private void initUI() {
        setTitle("Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel headerLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(70, 70, 70));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setOpaque(false);

        // Username panel
        JPanel usernamePanel = new JPanel(new BorderLayout(5, 2));
        usernamePanel.setOpaque(false);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField = new JTextField();
        styleTextField(usernameField);
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        // Password panel
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 2));
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        // Login button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(0, 120, 215));
        loginButton.addActionListener(this::performLogin);

        formPanel.add(usernamePanel);
        formPanel.add(passwordPanel);
        formPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    /**
     * Applies consistent styling to text fields.
     */
    private void styleTextField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setBackground(Color.WHITE);
    }

    /**
     * Styles buttons for UI consistency.
     */
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Handles login button click.
     */
    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            if (checkUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                Info infoScreen = new Info();
                infoScreen.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            showError("Database Error: " + ex.getMessage());
        }
    }

    /**
     * Checks user credentials against database.
     */
    public boolean checkUser(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedPassword);
            }
        }
        return false;
    }

    /**
     * Shows an error dialog.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, "<html><b>Error:</b><br>" + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Launch the Admin login screen.
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();

        EventQueue.invokeLater(() -> {
            try {
                First frame = new First();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
