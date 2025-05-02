import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckRoomStatusApp {
    private JFrame frame;
    private JTextField roomNumberField;
    private JButton btnCheckStatus;
    private JLabel statusLabel;

    public CheckRoomStatusApp() {
        initialize();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Room Status Checker");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        frame.add(panel, BorderLayout.CENTER);

        JLabel lblEnterRoomNumber = new JLabel("Enter Room Number:");
        lblEnterRoomNumber.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblEnterRoomNumber);

        roomNumberField = new JTextField();
        roomNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(roomNumberField);

        btnCheckStatus = new JButton("Check Room Status");
        btnCheckStatus.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckStatus.setBackground(new Color(0, 128, 255));
        btnCheckStatus.setForeground(Color.WHITE);
        btnCheckStatus.setFocusPainted(false);
        btnCheckStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkRoomStatus();
            }
        });
        panel.add(btnCheckStatus);

        statusLabel = new JLabel("Room Status: ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 25));
        frame.add(statusLabel, BorderLayout.SOUTH);
    }

    private void checkRoomStatus() {
        String roomNumber = roomNumberField.getText().trim();
        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a room number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String status = getRoomStatus(roomNumber);
        if (status != null) {
            statusLabel.setText("Room " + roomNumber + " Status: " + status);
        } else {
            statusLabel.setText("Room not found.");
        }
    }

    private String getRoomStatus(String roomNumber) {
        String status = null;
        String query = "SELECT status FROM Rooms WHERE room_number = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                status = rs.getString("status");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
}