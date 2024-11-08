package gui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TransactionPage extends JFrame {
    private JTextField categoryField, descriptionField, amountField;
    private JButton depositButton, withdrawButton;
    private HomePage homePage;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public TransactionPage(HomePage homePage) {
        this.homePage = homePage;

        setTitle("Transactions");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Top navigation bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Personal Finance", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        navBar.add(titleLabel, BorderLayout.CENTER);

        JButton homeButton = new JButton("Home");
        homeButton.setBackground(Color.LIGHT_GRAY);
        homeButton.addActionListener(e -> {
            dispose();
            homePage.setVisible(true);
        });
        navBar.add(homeButton, BorderLayout.WEST);

        add(navBar, BorderLayout.NORTH);

        // Left panel for transaction history
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Transaction History"));

        String[] columnNames = {"#", "Description", "Date", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);
        transactionTable.setDefaultRenderer(Object.class, new TransactionTableCellRenderer());

        // Initial loading of transactions into the table
        refreshTransactionTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(leftPanel, BorderLayout.WEST);

        // Right panel for adding transaction
        JPanel rightPanel = new JPanel(new GridLayout(5, 2));
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        rightPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        rightPanel.add(categoryField);

        rightPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        rightPanel.add(descriptionField);

        rightPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        rightPanel.add(amountField);

        depositButton = new JButton("Deposit");
        depositButton.addActionListener(e -> handleTransaction(true));
        withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e -> handleTransaction(false));

        rightPanel.add(depositButton);
        rightPanel.add(withdrawButton);

        add(rightPanel, BorderLayout.CENTER);
    }

    private void handleTransaction(boolean isDeposit) {
        String category = categoryField.getText();
        String description = descriptionField.getText();
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
            return;
        }

        if (!isDeposit && amount > homePage.getBalance()) {
            JOptionPane.showMessageDialog(this, "Insufficient funds for withdrawal.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String transactionDescription = category + " - " + description;
        double transactionAmount = isDeposit ? amount : -amount;
        homePage.addTransaction(transactionDescription, transactionAmount);
        JOptionPane.showMessageDialog(this, "Transaction added successfully!");

        // Clear the input fields
        categoryField.setText("");
        descriptionField.setText("");
        amountField.setText("");

        // Refresh the transaction table in the TransactionPage
        refreshTransactionTable(tableModel);
    }

    // Refresh the transaction table
    public void refreshTransactionTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear the table
        for (String[] trans : homePage.getTransactionList()) {
            tableModel.addRow(trans);
        }
        transactionTable.setDefaultRenderer(Object.class, new TransactionTableCellRenderer());
    }

    private class TransactionTableCellRenderer extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setOpaque(true);
            if (column == 3) { // Amount column
                setForeground(value.toString().startsWith("₹(") ? Color.RED : Color.GREEN);
            } else {
                setForeground(table.getForeground());
            }
            return this;
        }
    }
}