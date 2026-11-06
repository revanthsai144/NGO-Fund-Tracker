import java.util.ArrayList;
import java.util.List;

// Abstract class
abstract class Transaction {
        protected int id;
        protected double amount;
        protected String date;

        public Transaction(int id, double amount, String date) {
                this.id = id;
                this.amount = amount;
                this.date = date;
        }

        public double getAmount() {
                return amount;
        }

        abstract void displayDetails();
}

// Fund class
class Fund extends Transaction {

        private String donorName;

        public Fund(int id, double amount, String date, String donorName) {
                super(id, amount, date);
                this.donorName = donorName;
        }

        @Override
        void displayDetails() {
                System.out.println(
                                "Fund ID: " + id +
                                                ", Donor: " + donorName +
                                                ", Amount: ₹" + amount +
                                                ", Date: " + date);
        }
}

// Expenditure class
class Expenditure extends Transaction {

        private String description;

        public Expenditure(int id, double amount,
                        String date, String description) {
                super(id, amount, date);
                this.description = description;
        }

        @Override
        void displayDetails() {
                System.out.println(
                                "Expense ID: " + id +
                                                ", Description: " + description +
                                                ", Amount: ₹" + amount +
                                                ", Date: " + date);
        }
}

// Program class
class Program {

        private int programId;
        private String programName;
        private double budget;

        private List<Expenditure> expenditures;

        public Program(int programId, String programName, double budget) {
                this.programId = programId;
                this.programName = programName;
                this.budget = budget;
                expenditures = new ArrayList<>();
        }

        public void addExpenditure(Expenditure expenditure) {
                expenditures.add(expenditure);
        }

        public double getTotalExpenditure() {

                double total = 0;

                for (Expenditure e : expenditures) {
                        total += e.getAmount();
                }

                return total;
        }

        public double getUtilizationPercentage() {

                return (getTotalExpenditure() / budget) * 100;
        }

        public String getProgramName() {
                return programName;
        }

        public double getBudget() {
                return budget;
        }

        public List<Expenditure> getExpenditures() {
                return expenditures;
        }
}

// Anomaly Detector
class AnomalyDetector {

        public static String classify(double utilization) {

                if (utilization <= 90) {
                        return "NORMAL";
                } else if (utilization <= 100) {
                        return "WARNING";
                } else {
                        return "ANOMALOUS";
                }
        }

        public static void analyzeProgram(Program program) {

                double expenditure = program.getTotalExpenditure();
                double budget = program.getBudget();

                double utilization = program.getUtilizationPercentage();

                String classification = classify(utilization);

                System.out.println("\nProgram: "
                                + program.getProgramName());

                System.out.println("Approved Budget: ₹"
                                + budget);

                System.out.println("Actual Expenditure: ₹"
                                + expenditure);

                System.out.printf(
                                "Budget Utilization: %.2f%%%n",
                                utilization);

                System.out.println(
                                "Classification: " + classification);

                if (classification.equals("ANOMALOUS")) {

                        System.out.println(
                                        "⚠ ALERT: Unusual spending detected!");
                }
        }
}

// Main class
public class NGOFundTracker {

        public static void main(String[] args) {

                // Create fund records
                Fund fund1 = new Fund(
                                1,
                                500000,
                                "01-09-2026",
                                "ABC Foundation");

                Fund fund2 = new Fund(
                                2,
                                300000,
                                "02-09-2026",
                                "XYZ Trust");

                System.out.println("===== FUNDS RECEIVED =====");

                fund1.displayDetails();
                fund2.displayDetails();

                // Create programs

                Program education = new Program(
                                101,
                                "Education Program",
                                200000);

                Program healthcare = new Program(
                                102,
                                "Healthcare Program",
                                150000);

                Program food = new Program(
                                103,
                                "Food Distribution Program",
                                100000);

                // Add expenditures

                education.addExpenditure(
                                new Expenditure(
                                                1,
                                                100000,
                                                "03-09-2026",
                                                "Books and Study Material"));

                education.addExpenditure(
                                new Expenditure(
                                                2,
                                                50000,
                                                "04-09-2026",
                                                "School Equipment"));

                healthcare.addExpenditure(
                                new Expenditure(
                                                3,
                                                145000,
                                                "04-09-2026",
                                                "Medical Equipment"));

                food.addExpenditure(
                                new Expenditure(
                                                4,
                                                80000,
                                                "05-09-2026",
                                                "Food Purchase"));

                food.addExpenditure(
                                new Expenditure(
                                                5,
                                                40000,
                                                "06-09-2026",
                                                "Transportation"));

                // Analyze programs

                System.out.println(
                                "\n===== ANOMALY ANALYSIS =====");

                AnomalyDetector.analyzeProgram(education);

                AnomalyDetector.analyzeProgram(healthcare);

                AnomalyDetector.analyzeProgram(food);

        }
}