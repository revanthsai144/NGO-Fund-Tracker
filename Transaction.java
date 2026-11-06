// Abstract class
public abstract class Transaction implements Displayable {
    protected int id;
    protected double amount;
    protected String date;

    public Transaction(int id, double amount, String date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    @Override
    public abstract void displayDetails();
}
