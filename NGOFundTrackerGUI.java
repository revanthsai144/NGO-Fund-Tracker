import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NGOFundTrackerGUI extends JFrame {

    private List<Fund> funds = new ArrayList<>();
    private List<Program> programs = new ArrayList<>();

    // UI Tables
    private DefaultTableModel fundsTableModel;
    private DefaultTableModel expensesTableModel;
    private DefaultTableModel analysisTableModel;

    // Dropdown for selecting programs when adding expenditures
    private JComboBox<String> programDropdown;

    public NGOFundTrackerGUI() {
        setTitle("NGO Fund & Budget Tracker");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1. Funds", createFundsPanel());
        tabbedPane.addTab("2. Programs & Expenses", createProgramsPanel());
        tabbedPane.addTab("3. Anomaly Dashboard", createAnalysisPanel());

        add(tabbedPane);
    }

    // TAB 1: FUNDS PANEL
    private JPanel createFundsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        JTextField idField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField donorField = new JTextField();
        JButton addBtn = new JButton("Add Fund");

        form.add(new JLabel("Fund ID:"));
        form.add(idField);
        form.add(new JLabel("Amount (Rs.):"));
        form.add(amountField);
        form.add(new JLabel("Date (DD-MM-YYYY):"));
        form.add(dateField);
        form.add(new JLabel("Donor Name:"));
        form.add(donorField);
        form.add(new JLabel(""));
        form.add(addBtn);

        // Table
        fundsTableModel = new DefaultTableModel(new String[]{"Fund ID", "Donor", "Amount (Rs.)", "Date"}, 0);
        JTable table = new JTable(fundsTableModel);

        addBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());
                String date = dateField.getText().trim();
                String donor = donorField.getText().trim();

                Fund fund = new Fund(id, amount, date, donor);
                funds.add(fund);
                fundsTableModel.addRow(new Object[]{id, donor, amount, date});

                idField.setText("");
                amountField.setText("");
                dateField.setText("");
                donorField.setText("");
                JOptionPane.showMessageDialog(this, "Fund recorded successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // TAB 2: PROGRAMS & EXPENDITURES
    private JPanel createProgramsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: Add Program
        JPanel progPanel = new JPanel(new BorderLayout(5, 5));
        progPanel.setBorder(BorderFactory.createTitledBorder("Create Program"));
        JPanel progForm = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField progIdField = new JTextField();
        JTextField progNameField = new JTextField();
        JTextField progBudgetField = new JTextField();
        JButton addProgBtn = new JButton("Create Program");

        progForm.add(new JLabel("Program ID:"));
        progForm.add(progIdField);
        progForm.add(new JLabel("Program Name:"));
        progForm.add(progNameField);
        progForm.add(new JLabel("Budget (Rs.):"));
        progForm.add(progBudgetField);
        progForm.add(new JLabel(""));
        progForm.add(addProgBtn);

        progPanel.add(progForm, BorderLayout.NORTH);

        // Right: Add Expenditure
        JPanel expPanel = new JPanel(new BorderLayout(5, 5));
        expPanel.setBorder(BorderFactory.createTitledBorder("Add Expenditure"));

        JPanel expForm = new JPanel(new GridLayout(6, 2, 5, 5));
        programDropdown = new JComboBox<>();
        JTextField expIdField = new JTextField();
        JTextField expAmountField = new JTextField();
        JTextField expDateField = new JTextField();
        JTextField expDescField = new JTextField();
        JButton addExpBtn = new JButton("Add Expense");

        expForm.add(new JLabel("Select Program:"));
        expForm.add(programDropdown);
        expForm.add(new JLabel("Expense ID:"));
        expForm.add(expIdField);
        expForm.add(new JLabel("Amount (Rs.):"));
        expForm.add(expAmountField);
        expForm.add(new JLabel("Date:"));
        expForm.add(expDateField);
        expForm.add(new JLabel("Description:"));
        expForm.add(expDescField);
        expForm.add(new JLabel(""));
        expForm.add(addExpBtn);

        expensesTableModel = new DefaultTableModel(new String[]{"Program", "ID", "Description", "Amount", "Date"}, 0);
        JTable expTable = new JTable(expensesTableModel);
        expPanel.add(expForm, BorderLayout.NORTH);
        expPanel.add(new JScrollPane(expTable), BorderLayout.CENTER);

        // Listeners
        addProgBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(progIdField.getText().trim());
                String name = progNameField.getText().trim();
                double budget = Double.parseDouble(progBudgetField.getText().trim());

                Program prog = new Program(id, name, budget);
                programs.add(prog);
                programDropdown.addItem(name);

                progIdField.setText("");
                progNameField.setText("");
                progBudgetField.setText("");
                refreshAnalysis();
                JOptionPane.showMessageDialog(this, "Program created!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid program inputs!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addExpBtn.addActionListener(e -> {
            try {
                int selectedIdx = programDropdown.getSelectedIndex();
                if (selectedIdx == -1) {
                    JOptionPane.showMessageDialog(this, "Please create and select a program first.");
                    return;
                }
                int expId = Integer.parseInt(expIdField.getText().trim());
                double amount = Double.parseDouble(expAmountField.getText().trim());
                String date = expDateField.getText().trim();
                String desc = expDescField.getText().trim();

                Program prog = programs.get(selectedIdx);
                Expenditure exp = new Expenditure(expId, amount, date, desc);
                prog.addExpenditure(exp);

                expensesTableModel.addRow(new Object[]{prog.getProgramName(), expId, desc, amount, date});
                expIdField.setText("");
                expAmountField.setText("");
                expDateField.setText("");
                expDescField.setText("");

                refreshAnalysis();
                JOptionPane.showMessageDialog(this, "Expenditure added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid expenditure input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(progPanel);
        panel.add(expPanel);
        return panel;
    }

    // TAB 3: ANOMALY ANALYSIS DASHBOARD
    private JPanel createAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        analysisTableModel = new DefaultTableModel(
            new String[]{"Program", "Approved Budget (Rs.)", "Total Spent (Rs.)", "Utilization (%)", "Status"}, 0
        );
        JTable table = new JTable(analysisTableModel);

        JButton refreshBtn = new JButton("Refresh Analysis");
        refreshBtn.addActionListener(e -> refreshAnalysis());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAnalysis() {
        analysisTableModel.setRowCount(0);
        for (Program p : programs) {
            double budget = p.getBudget();
            double spent = p.getTotalExpenditure();
            double util = p.getUtilizationPercentage();
            String status = AnomalyDetector.classifyUtilization(util);

            analysisTableModel.addRow(new Object[]{
                p.getProgramName(),
                String.format("%.2f", budget),
                String.format("%.2f", spent),
                String.format("%.2f%%", util),
                status
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NGOFundTrackerGUI().setVisible(true);
        });
    }
}
