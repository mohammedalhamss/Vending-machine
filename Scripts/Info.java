package vending_M_F;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Product Management UI for admins within the vending machine system.
 * Supports adding, deleting, updating, and displaying product information.
 */
public class Info extends JFrame {

    private JTextField nameField, priceField, quantityField, expiryField;
    private JTextField deleteIdField, updateIdField, updateQuantityField;
    private JComboBox<String> nameComboBox, operationComboBox;
    private JTextArea outputArea;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    /**
     * Constructor sets up the frame and all functional panels.
     */
    public Info() {
        setTitle("Product Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // FlatLaf styling theme
        FlatLightLaf.setup();

        // Main container panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Navigation buttons
        JPanel navPanel = createNavPanel();
        mainPanel.add(navPanel, BorderLayout.NORTH);

        // Card-based content area
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.add(createAddPanel(), "ADD_PRODUCT");
        cardPanel.add(createDeletePanel(), "DELETE_PRODUCT");
        cardPanel.add(createUpdatePanel(), "UPDATE_STOCK");
        cardPanel.add(createDetailsPanel(), "PRODUCT_DETAILS");
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Output area for feedback
        outputArea = new JTextArea();
        styleTextArea(outputArea);
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        navPanel.setOpaque(false);

        String[] buttons = {"Add Product", "Delete Product", "Update Stock", "Product Details", "Exit"};
        for (String text : buttons) {
            JButton btn = new JButton(text);
            styleButton(btn, new Color(70, 130, 180));
            btn.addActionListener(e -> {
                if (text.equals("Exit")) {
                    dispose();
                } else {
                    cardLayout.show(cardPanel, text.toUpperCase().replace(" ", "_"));
                }
            });
            navPanel.add(btn);
        }
        return navPanel;
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        setupPanelBasics(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        setupGrid(gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Add New Product"), gbc);

        // Create field rows
        addFormRow(panel, gbc, "Product Name:", nameField = new JTextField());
        addFormRow(panel, gbc, "Price (₺):", priceField = new JTextField());
        addFormRow(panel, gbc, "Quantity:", quantityField = new JTextField());
        addFormRow(panel, gbc, "Expiry Date (YYYY-MM-DD):", expiryField = new JTextField());

        gbc.gridy++;
        JButton addBtn = new JButton("Add Product");
        styleButton(addBtn, new Color(76, 175, 80));
        addBtn.addActionListener(this::addProduct);
        panel.add(addBtn, gbc);

        return panel;
    }

    private JPanel createDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        setupPanelBasics(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        setupGrid(gbc);

        panel.add(new JLabel("Delete Product"), gbc);
        addFormRow(panel, gbc, "Product ID:", deleteIdField = new JTextField());

        gbc.gridy++;
        JButton deleteBtn = new JButton("Delete Product");
        styleButton(deleteBtn, new Color(244, 67, 54));
        deleteBtn.addActionListener(this::deleteProduct);
        panel.add(deleteBtn, gbc);

        return panel;
    }

    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        setupPanelBasics(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        setupGrid(gbc);

        panel.add(new JLabel("Update Stock"), gbc);
        addFormRow(panel, gbc, "Product ID:", updateIdField = new JTextField(15));
        addFormRow(panel, gbc, "New Quantity:", updateQuantityField = new JTextField(15));

        gbc.gridy++;
        JButton updateBtn = new JButton("Update Stock");
        styleButton(updateBtn, new Color(255, 152, 0));
        updateBtn.addActionListener(this::updateProduct);
        panel.add(updateBtn, gbc);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        setupPanelBasics(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        setupGrid(gbc);

        panel.add(new JLabel("Product Details"), gbc);
        addComboRow(panel, gbc, "Product Name:", nameComboBox = new JComboBox<>());
        nameComboBox.setPrototypeDisplayValue("Select a product");
        styleComboBox(nameComboBox);

        addComboRow(panel, gbc, "Operation:", operationComboBox = new JComboBox<>(new String[]{"View Details", "Check Stock"}));
        styleComboBox(operationComboBox);

        gbc.gridy++;
        JButton executeBtn = new JButton("Execute");
        styleButton(executeBtn, new Color(103, 58, 183));
        executeBtn.addActionListener(this::showDetails);
        panel.add(executeBtn, gbc);

        loadProductsIntoComboBox();
        return panel;
    }

    // Shared method to style text area
    private void styleTextArea(JTextArea area) {
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        area.setBackground(Color.WHITE);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    private void setupPanelBasics(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);
    }

    private void setupGrid(GridBagConstraints gbc) {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        panel.add(new JLabel(label), gbc);
        gbc.gridx++;
        styleTextField(field = field != null ? field : new JTextField());
        panel.add(field, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private void addComboRow(JPanel panel, GridBagConstraints gbc, String label, JComboBox<String> combo) {
        panel.add(new JLabel(label), gbc);
        gbc.gridx++;
        panel.add(combo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        Dimension dim = new Dimension(250, 30);
        field.setPreferredSize(dim);
        field.setMinimumSize(dim);
        field.setMaximumSize(dim);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    public Connection getConnected() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/vending_machine", "root", "Akram1305@");
    }

    // Implement addProduct(), deleteProduct(), updateProduct(), showDetails(), loadProductsIntoComboBox(),
    // save(), delete(), and clearFields() as in your original, using same logic and styles

    public static void main(String[] args) {
        FlatLightLaf.setup();
        EventQueue.invokeLater(() -> {
            Info frame = new Info();
            frame.setVisible(true);
        });
    }
}
