import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {
    private List<Expense> expenses = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> expenseList;
    private JTextField descriptionField, amountField;
    private JComboBox<String> categoryComboBox;

    private static final String FILE_NAME = "expenses.txt";

    public ExpenseTrackerGUI() {
        setTitle("Expense Tracker");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load expenses from file
        loadExpenses();

        // Setup components
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField();

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>(new String[]{"Food", "Transport", "Entertainment", "Other"});

        JButton addButton = new JButton("Add Expense");

        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionField);
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);
        inputPanel.add(new JLabel());
        inputPanel.add(addButton);

        expenseList = new JList<>(listModel);
        updateExpenseList();

        JButton deleteButton = new JButton("Delete Selected Expense");
        JButton summaryButton = new JButton("Show Summary");

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(expenseList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(summaryButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedExpense();
            }
        });

        summaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExpenseSummary();
            }
        });

        setVisible(true);
    }

    private void addExpense() {
        String description = descriptionField.getText();
        double amount = Double.parseDouble(amountField.getText());
        String categoryString = (String) categoryComboBox.getSelectedItem();
        ExpenseCategory category = ExpenseCategory.valueOf(categoryString.toUpperCase());

        Expense expense = new Expense(description, amount, category);
        expenses.add(expense);
        listModel.addElement(expense.toString());
        saveExpenses();

        descriptionField.setText("");
        amountField.setText("");
        categoryComboBox.setSelectedIndex(0);
    }

    private void deleteSelectedExpense() {
        int selectedIndex = expenseList.getSelectedIndex();
        if (selectedIndex != -1) {
            expenses.remove(selectedIndex);
            listModel.remove(selectedIndex);
            saveExpenses();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.");
        }
    }

    private void showExpenseSummary() {
        double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();
        JOptionPane.showMessageDialog(this, "Total Expenses: $" + totalAmount);
    }

    private void updateExpenseList() {
        listModel.clear();
        for (Expense expense : expenses) {
            listModel.addElement(expense.toString());
        }
    }

    private void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String description = parts[0];
                double amount = Double.parseDouble(parts[1]);
                ExpenseCategory category = ExpenseCategory.valueOf(parts[2]);
                expenses.add(new Expense(description, amount, category));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + e.getMessage());
        }
    }

    private void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.write(expense.getDescription() + "," + expense.getAmount() + "," + expense.getCategory());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving expenses: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }
}
