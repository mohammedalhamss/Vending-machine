package vending_M_F;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * The Machine class represents the vending machine UI where users can select products,
 * input money, and make purchases. It handles display, user input, and database updates.
 */
public class Machine extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private JTextField moneyField;
    private ArrayList<items> itemsList;

    /**
     * Establishes a connection to the MySQL database.
     * @return SQL Connection
     * @throws SQLException if connection fails
     */
    public Connection getConnected() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/vending_machine", "root", "Akram1305@");
    }

    /**
     * Main entry point. Sets up the look and feel and launches the main screen.
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    first frame = new first();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Constructor that initializes the vending machine window with a product list.
     * @param itemsList List of available items
     */
    public Machine(ArrayList<items> itemsList) {
        this.itemsList = itemsList;

        setTitle("Vending Machine - Product Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Available Products");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 70, 70));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Back button to return to main screen
        JButton backButton = new JButton("Back to Main");
        styleButton(backButton, new Color(100, 100, 100), 14);
        backButton.addActionListener(e -> {
            try {
                new first().setVisible(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            dispose();
        });
        headerPanel.add(backButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table for displaying products
        String[] columnNames = {"Product Name", "Price (₺)", "Stock"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (items item : itemsList) {
            Object[] row = {item.getName(), item.getPrice(), item.getStock()};
            model.addRow(row);
        }

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Right panel for money input and purchase button
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(250, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel moneyPanel = new JPanel(new BorderLayout(5, 5));
        moneyPanel.setOpaque(false);

        JLabel moneyLabel = new JLabel("Insert Money (₺):");
        moneyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        moneyPanel.add(moneyLabel, BorderLayout.NORTH);

        moneyField = new JTextField();
        moneyField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        moneyField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        moneyPanel.add(moneyField, BorderLayout.CENTER);
        rightPanel.add(moneyPanel, gbc);

        JButton buyButton = new JButton("Buy Product");
        styleButton(buyButton, new Color(0, 120, 215), 16);
        buyButton.addActionListener(this::buyProduct);
        rightPanel.add(buyButton, gbc);

        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel);
    }

    /**
     * Applies consistent styling to buttons.
     */
    private void styleButton(JButton button, Color color, int fontSize) {
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Handles purchase logic: validates money, checks stock, warns on expiry,
     * updates stock in DB, and shows purchase summary.
     */
    private void buyProduct(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a product first.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double money = Double.parseDouble(moneyField.getText());
            items selectedItem = itemsList.get(selectedRow);
            double itemPrice = selectedItem.getPrice();

            if (money < itemPrice) {
                JOptionPane.showMessageDialog(this, 
                    String.format("Not enough money. Price: ₺%.2f", itemPrice), 
                    "Insufficient Funds", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            } else if (selectedItem.getStock() <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "This item is out of stock.", 
                    "Out of Stock", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.Date expiry = selectedItem.getExpiryDate();
            java.time.LocalDate expiryLocal = ((java.sql.Date) expiry).toLocalDate();
            java.time.LocalDate today = java.time.LocalDate.now();

            if (expiryLocal.isBefore(today.plusMonths(6))) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "⚠ Warning: This item expires in less than 6 months!\nDo you still want to purchase it?",
                    "Expiry Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) return;
            }

            updateStock(selectedItem.getId());
            selectedItem.setStock(selectedItem.getStock() - 1);
            insertSale(selectedItem.getId());

            double change = money - itemPrice;
            String message = "<html><div style='text-align:center;'>" +
                             "<h3>Thank you for your purchase!</h3>" +
                             "<p><b>Item:</b> " + selectedItem.getName() + "<br>" +
                             "<b>Price:</b> ₺" + itemPrice + "</p>";
            if (change > 0) {
                message += "<p><b>Change:</b> ₺" + change + "</p>";
            }
            message += "</div></html>";

            JOptionPane.showMessageDialog(this, message, "Purchase Complete", JOptionPane.INFORMATION_MESSAGE);

            moneyField.setText("");
            ((DefaultTableModel)table.getModel()).setValueAt(selectedItem.getStock(), selectedRow, 2);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid amount of money.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates stock in the database for a purchased item.
     */
    private void updateStock(int productId) {
        try {
            PreparedStatement pst = getConnected().prepareStatement(
                "UPDATE products SET stock = stock - 1 WHERE id = ?"
            );
            pst.setInt(1, productId);
            pst.executeUpdate();
            getConnected().close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error updating stock: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inserts a new sale record in the database.
     */
    private void insertSale(int productId) {
        try {
            String insertSql = "INSERT INTO sales (product_id, quantity, Date) VALUES (?, 1, ?)";
            PreparedStatement insertPst = getConnected().prepareStatement(insertSql);
            insertPst.setInt(1, productId);
            insertPst.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            insertPst.executeUpdate();
            getConnected().close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error recording sale: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
