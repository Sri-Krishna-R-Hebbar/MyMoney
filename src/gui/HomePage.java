package gui;

import models.User;
import models.UserDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class HomePage extends JFrame {
    private JLabel balanceLabel, incomeLabel;
    private double balance;
    private ArrayList<String[]> transactionList;
    private User user;

    public HomePage(User user) {
        this.user = user;
        this.balance = user.getAmount();
        this.transactionList = new ArrayList<>();
        
        setTitle("Personal Finance - Home");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top navigation bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Personal Finance", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        navBar.add(titleLabel, BorderLayout.CENTER);

        JButton transactionButton = new JButton("Transaction");
        transactionButton.setBackground(Color.LIGHT_GRAY);
        transactionButton.addActionListener(e -> new TransactionPage(this).setVisible(true));
        navBar.add(transactionButton, BorderLayout.WEST);

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setBackground(Color.LIGHT_GRAY);
        signOutButton.addActionListener(e -> {
            dispose();
            new LoginPage(new UserDatabase()).setVisible(true);
        });
        navBar.add(signOutButton, BorderLayout.EAST);

        add(navBar, BorderLayout.NORTH);

        // Left panel with user details
        JPanel leftPanel = new JPanel(new GridLayout(8, 1));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        leftPanel.add(new JLabel("Name: " + user.getName()));
        leftPanel.add(new JLabel("DOB: " + user.getDob()));
        leftPanel.add(new JLabel("Aadhaar Number: " + user.getAadhaar()));
        leftPanel.add(new JLabel("Acc No: " + user.getEmail()));
        leftPanel.add(new JLabel("Bank: " + user.getBank()));
        leftPanel.add(new JLabel("Branch: " + user.getBranch()));
        leftPanel.add(new JLabel("Acc Type: " + user.getAccountType()));
        leftPanel.add(new JLabel("Address: " + user.getAddress()));
        
        add(leftPanel, BorderLayout.WEST);

        // Right panel with balance and transactions
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(Color.LIGHT_GRAY);
        balancePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        balanceLabel = new JLabel("Current Balance: ₹" + balance);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        incomeLabel = new JLabel("Month Income: ₹30000");
        incomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balancePanel.add(balanceLabel);
        balancePanel.add(incomeLabel);

        rightPanel.add(balancePanel);

        // Table for previous transactions
        String[] columnNames = {"#", "Description", "Date", "Amount"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable transactionTable = new JTable(tableModel);
        
        for (int i = 0; i < transactionList.size(); i++) {
            tableModel.addRow(transactionList.get(i));
        }
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Previous Transactions"));
        rightPanel.add(scrollPane);

        add(rightPanel, BorderLayout.CENTER);
    }

    public void addTransaction(String description, double amount) {
        balance += amount;
        balanceLabel.setText("Current Balance: ₹" + balance);

        String[] transaction = {
            String.valueOf(transactionList.size() + 1),
            description,
            "10-11-2024",  // Placeholder date
            String.format("₹%.2f", amount)
        };
        transactionList.add(transaction);
    }

    public double getBalance() {
        return balance;
    }
    
    public ArrayList<String []> getTransactionList() {
        return transactionList;
    }

}
