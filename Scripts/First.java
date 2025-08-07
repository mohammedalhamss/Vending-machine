package vending_M_F;

// Required imports
import java.awt.EventQueue;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * First screen of the vending machine application.
 * Offers options to either shop or log in as an admin.
 */
public class First extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Main method to launch the application with modern FlatLaf look.
     */
    public static void main(String[] args) {
        FlatLightLaf.setup(); // Set FlatLaf theme

        EventQueue.invokeLater(() -> {
            try {
                First frame = new First(); // Open the initial frame
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Establish connection to MySQL database.
     * 
     * @return Connection object
     * @throws SQLException if database connection fails
     */
    public Connection getConnected() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/vending_machine",
            "root",
            "Akram1305@"
        );
    }

    /**
     * Retrieve list of products (items) from the database.
     * 
     * @return ArrayList of items
     */
    public ArrayList<items> getItems() {
        ArrayList<items> list = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try {
            PreparedStatement pst = getConnected().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                items it = new items();
                it.setId(rs.getInt("id"));
                it.setName(rs.getString("name"));
                it.setPrice(rs.getInt("price"));
                it.setStock(rs.getInt("stock"));
                it.setExpiryDate(rs.getDate("expiry_date"));

                list.add(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Applies consistent styling to buttons.
     */
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Constructor to set up the first window with buttons for shopping or admin login.
     */
    public First() throws SQLException {
        setTitle("Vending Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null); // Center window

        // Main layout panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Welcome header
        JLabel welcomeLabel = new JLabel("Welcome to Vending Machine", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(70, 70, 70));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Button section
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        // Start Shopping Button
        JButton startButton = new JButton("Start Shopping");
        styleButton(startButton, new Color(0, 120, 215)); // Blue
        startButton.addActionListener(e -> {
            ArrayList<items> items = getItems(); // Fetch items from DB
            machine frame = new machine(items); // Open machine window
            frame.setVisible(true);
            dispose(); // Close current window
        });

        // Admin Login Button
        JButton adminButton = new JButton("Admin Login");
        styleButton(adminButton, new Color(100, 100, 100)); // Gray
        adminButton.addActionListener(e -> {
            Admin frame = new Admin(); // Open admin login window
            frame.setVisible(true);
            dispose(); // Close current window
        });

        // Add buttons to panel
        buttonPanel.add(startButton);
        buttonPanel.add(adminButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }
}
