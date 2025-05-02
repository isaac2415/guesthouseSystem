import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class WorkerManager extends JFrame {
    private JTextField nameField, roleField, phoneField, addressField, salaryField;
    private JButton addButton, deleteButton, viewButton;

    private final String DB_URL = "jdbc:mysql://localhost:3306/isaac_guesthouse";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    public WorkerManager() {
        setTitle("Worker Management");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Guesthouse Worker Management", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        //formPanel.setAlwaysOnTop(true);
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Role:"));
        roleField = new JTextField();
        formPanel.add(roleField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        formPanel.add(new JLabel("Salary:"));
        salaryField = new JTextField();
        formPanel.add(salaryField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Worker");
        deleteButton = new JButton("Delete by Name");
        viewButton = new JButton("View Workers");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(_ -> addWorker());
        deleteButton.addActionListener(_ -> deleteWorker());
        viewButton.addActionListener(_ -> viewWorkers());

        setVisible(true);
    }

    private void addWorker() {
        String name = nameField.getText().trim();
        String role = roleField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String salaryText = salaryField.getText().trim();

        if (name.isEmpty() || role.isEmpty() || phone.isEmpty() || address.isEmpty() || salaryText.isEmpty()) {
            showMessage("Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "INSERT INTO workers (name, role, phone, address, salary) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, role);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setDouble(5, salary);

            int rows = stmt.executeUpdate();
            conn.close();

            if (rows > 0) {
                showMessage("Worker added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        } catch (NumberFormatException ex) {
            showMessage("Invalid salary. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showMessage("Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteWorker() {
        String name = JOptionPane.showInputDialog(this, "Enter worker name to delete:");
        if (name == null || name.trim().isEmpty()) return;

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "DELETE FROM workers WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name.trim());

            int rows = stmt.executeUpdate();
            conn.close();

            if (rows > 0) {
                showMessage("Worker deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessage("No worker found with that name.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            showMessage("Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewWorkers() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT name, role, phone, address, salary FROM workers");
    
            ResultSetMetaData rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            String[] columnNames = new String[columns];
            for (int i = 1; i <= columns; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);
            }
    
            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();
    
            Object[][] data = new Object[rows][columns];
            int rowIndex = 0;
            while (rs.next()) {
                for (int col = 1; col <= columns; col++) {
                    data[rowIndex][col - 1] = rs.getObject(col);
                }
                rowIndex++;
            }
    
            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
    
            JDialog dialog = new JDialog(this, "All Workers", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.add(scrollPane);
            dialog.setVisible(true);
    
            conn.close();
        } catch (SQLException ex) {
            showMessage("Error loading workers: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    private void clearFields() {
        nameField.setText("");
        roleField.setText("");
        phoneField.setText("");
        addressField.setText("");
        salaryField.setText("");
    }

    public static void view() {
        SwingUtilities.invokeLater(() -> new WorkerManager());
    }
}
