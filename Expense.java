public class Expense {
    private String description;
    private double amount;
    private ExpenseCategory category;

    public Expense(String description, double amount, ExpenseCategory category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("Description: %s, Amount: $%.2f, Category: %s", description, amount, category);
    }
}
