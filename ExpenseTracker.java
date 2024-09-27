import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private static List<Expense> expenses = new ArrayList<>();
    private static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {
        loadExpenses(); // Load expenses from file
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Add Expense\n2. View Expenses\n3. Delete Expense\n4. Expense Summary\n5. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1 -> addExpense(scanner);
                case 2 -> viewExpenses();
                case 3 -> deleteExpense(scanner);
                case 4 -> showExpenseSummary();
                case 5 -> {
                    saveExpenses(); // Save expenses to file
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addExpense(Scanner scanner) {
        scanner.nextLine(); // consume newline
        System.out.print("Enter expense description: ");
        String description = scanner.nextLine();

        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();

        System.out.println("Choose category: 1. Food 2. Transport 3. Entertainment 4. Other");
        int categoryOption = scanner.nextInt();
        ExpenseCategory category = switch (categoryOption) {
            case 1 -> ExpenseCategory.FOOD;
            case 2 -> ExpenseCategory.TRANSPORT;
            case 3 -> ExpenseCategory.ENTERTAINMENT;
            default -> ExpenseCategory.OTHER;
        };

        expenses.add(new Expense(description, amount, category));
        System.out.println("Expense added successfully.");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            System.out.println("Expenses:");
            for (int i = 0; i < expenses.size(); i++) {
                System.out.println((i + 1) + ". " + expenses.get(i));
            }
        }
    }

    private static void deleteExpense(Scanner scanner) {
        viewExpenses();
        if (!expenses.isEmpty()) {
            System.out.print("Enter expense number to delete: ");
            int expenseNumber = scanner.nextInt();
            if (expenseNumber > 0 && expenseNumber <= expenses.size()) {
                expenses.remove(expenseNumber - 1);
                System.out.println("Expense deleted.");
            } else {
                System.out.println("Invalid expense number.");
            }
        }
    }

    private static void showExpenseSummary() {
        Map<ExpenseCategory, Double> summary = new HashMap<>();
        double totalAmount = 0;

        for (Expense expense : expenses) {
            summary.put(expense.getCategory(), summary.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
            totalAmount += expense.getAmount();
        }

        System.out.println("Expense Summary by Category:");
        for (var entry : summary.entrySet()) {
            System.out.printf("%s: $%.2f\n", entry.getKey(), entry.getValue());
        }
        System.out.printf("Total: $%.2f\n", totalAmount);
    }

    private static void loadExpenses() {
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
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }

    private static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.write(expense.getDescription() + "," + expense.getAmount() + "," + expense.getCategory());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }
}
