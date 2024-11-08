package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionPage extends JFrame {
    private JTextField categoryField, descriptionField, dateField, amountField;
    private JButton depositButton, withdrawButton;
    private HomePage homePage;

    public TransactionPage(HomePage homePage) {
        this.homePage = homePage;

        setTitle("Add Transaction");
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
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable transactionTable = new JTable(tableModel);

        for (int i = 0; i < homePage.getTransactionList().size(); i++) {
            tableModel.addRow(homePage.getTransactionList().get(i));
        }

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

        rightPanel.add(new JLabel("Date (dd-mm-yyyy):"));
        dateField = new JTextField("10-11-2024");  // Default date as placeholder
        rightPanel.add(dateField);

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
        String date = dateField.getText();
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
            return;
        }

        if (!isDeposit && amount > homePage.getBalance()) {
            JOptionPane.showMessageDialog(this, "Insufficient funds for withdrawal.");
            return;
        }

        String transactionDescription = category + " - " + description;
        double transactionAmount = isDeposit ? amount : -amount;
        homePage.addTransaction(transactionDescription, transactionAmount);
        JOptionPane.showMessageDialog(this, "Transaction added successfully!");

        // No dispose here, just allow the user to return using Home button
    }
}
