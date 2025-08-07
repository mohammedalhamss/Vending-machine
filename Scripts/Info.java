package vending_M_F;
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    /**
     * Applies background and spacing to a panel.
     */
    private void setupPanelBasics(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);
    }

    /**
     * Initializes common grid constraints.
     */
    private void setupGrid(GridBagConstraints gbc) {
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
    }

    /**
     * Adds a label + text field row to a panel.
     */
    private void addFormRow(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        panel.add(new JLabel(label), gbc);
        gbc.gridx++;
        styleTextField(field);
        panel.add(field, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    /**
     * Adds a label + combo box row to a panel.
     */
    private void addComboRow(JPanel panel, GridBagConstraints gbc, String label, JComboBox<String> combo) {
        panel.add(new JLabel(label), gbc);
        gbc.gridx++;
        panel.add(combo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    /**
     * Styles text field inputs.
     */
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

    /**
     * Styles functional buttons.
     */
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles combo boxes.
     */
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    /**
     * Establishes DB connection.
     */
    public Connection getConnected() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/vending_machine", "root", "Akram1305@");
    }

    /**
     * Entry point to launch the product management UI.
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();
        EventQueue.invokeLater(() -> {
            Info frame = new Info();
            frame.setVisible(true);
        });
    }

   
}
