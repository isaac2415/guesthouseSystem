import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeRoomStatusApp {
    private JFrame frame;
    private JTextField roomNumberField;
    private JButton btnCheckOut;
    private JLabel statusLabel;

  
    public ChangeRoomStatusApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Room Status Manager");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        frame.add(panel, BorderLayout.CENTER);

        JLabel lblEnterRoomNumber = new JLabel("Enter Room Number To Make it Available:");
        lblEnterRoomNumber.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblEnterRoomNumber);

        roomNumberField = new JTextField();
        roomNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(roomNumberField);

        btnCheckOut = new JButton("Check-Out (Set Available)");
        btnCheckOut.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckOut.setBackground(new Color(0, 128, 255));
        btnCheckOut.setForeground(Color.WHITE);
        btnCheckOut.setFocusPainted(false);
        btnCheckOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoomStatus();
            }
        });
        panel.add(btnCheckOut);

        statusLabel = new JLabel("Room Status: ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 25));
        frame.add(statusLabel, BorderLayout.SOUTH);
    }

    private void updateRoomStatus() {
        String roomNumber = roomNumberField.getText().trim();
        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a room number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean success = changeRoomStatusToAvailable(roomNumber);
        if (success) {
            statusLabel.setText("Room " + roomNumber + " status changed to Available.");
        } else {
            statusLabel.setText("Room is already in Available state.");
        }
    }

    private boolean changeRoomStatusToAvailable(String roomNumber) {
        boolean success = false;
        String query = "UPDATE Rooms SET status = 'available' WHERE room_number = ? AND status = 'occupied'";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roomNumber);
            int rowsUpdated = stmt.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }

    public void showWindow() {
        frame.setVisible(true);
    }
}
