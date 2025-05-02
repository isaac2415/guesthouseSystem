import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewTotalMoneyApp {
    private JFrame frame;
    private JTextField monthField;
    private JButton btnShowTotal;
    private JLabel totalMoneyLabel;

    public ViewTotalMoneyApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Total Money Viewer");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        frame.add(panel, BorderLayout.CENTER);

        JLabel lblSelectMonth = new JLabel("Enter Month (YYYY-MM) or leave blank for all-time:");
        lblSelectMonth.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblSelectMonth);

        monthField = new JTextField();
        monthField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(monthField);

        btnShowTotal = new JButton("Show Total Money");
        btnShowTotal.setFont(new Font("Arial", Font.BOLD, 14));
        btnShowTotal.setBackground(new Color(0, 128, 255));
        btnShowTotal.setForeground(Color.WHITE);
        btnShowTotal.setFocusPainted(false);
        btnShowTotal.addActionListener(_ -> showTotalMoney());
        panel.add(btnShowTotal);

        totalMoneyLabel = new JLabel("Total Money: ", SwingConstants.CENTER);
        totalMoneyLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        frame.add(totalMoneyLabel, BorderLayout.SOUTH);
    }

    private void showTotalMoney() {
        String month = monthField.getText().trim();
        double totalMoney = calculateTotalMoney(month);
        totalMoneyLabel.setText("Total Money: " + totalMoney);
    }

    private double calculateTotalMoney(String month) {
        double totalMoney = 0.0;
        String query;
        if (month.isEmpty()) {
            query = "SELECT SUM(total_amount_paid) AS total FROM Bookings";
        } else {
            query = "SELECT SUM(total_amount_paid) AS total FROM Bookings WHERE DATE_FORMAT(start_time, '%Y-%m') = ?";
        }

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (!month.isEmpty()) {
                stmt.setString(1, month);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalMoney = rs.getDouble("total");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalMoney;
    }


    public void showWindow() {
        frame.setVisible(true);
    }
}
