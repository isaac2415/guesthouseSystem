import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookApp {
    private JFrame frame;
    private JPanel panel;
    private JTextField guestNameField, phoneField, roomNumberField;
    private JComboBox<String> roomTypeComboBox;
    private JButton btnBookRoom;

    public BookApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Guesthouse Booking");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel lblGuestName = new JLabel("Guest Name:");
        lblGuestName.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblGuestName, gbc);

        gbc.gridx = 1;
        guestNameField = new JTextField(15);
        guestNameField.setFont(new Font("Arial", Font.PLAIN, 15));
        guestNameField.setPreferredSize(new Dimension(200, 40));
        panel.add(guestNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblPhone, gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 15));
        phoneField.setPreferredSize(new Dimension(200,40));
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblRoomNumber = new JLabel("Room Number:");
        lblRoomNumber.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(lblRoomNumber, gbc);

        gbc.gridx = 1;
        roomNumberField = new JTextField(15);
        roomNumberField.setFont(new Font("Arial", Font.PLAIN, 15));
        roomNumberField.setPreferredSize(new Dimension(200,40));
        panel.add(roomNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblRoomType = new JLabel("Room Type:");
        lblRoomType.setFont(new Font("Arial", Font.BOLD, 14));
        //panel.add(lblRoomType, gbc);

        gbc.gridx = 1;
        String[] roomTypes = {"Small_5000", "Medium_10000", "Big_20000"};
        roomTypeComboBox = new JComboBox<>(roomTypes);
        roomTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        //panel.add(roomTypeComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        btnBookRoom = new JButton("Book Room");
        btnBookRoom.setFont(new Font("Arial", Font.BOLD, 14));
        btnBookRoom.setBackground(new Color(0, 102, 204));
        btnBookRoom.setPreferredSize(new Dimension(200, 50));
        btnBookRoom.setForeground(Color.WHITE);
        btnBookRoom.setFocusPainted(false);
        btnBookRoom.setBorder(BorderFactory.createRaisedBevelBorder());
        btnBookRoom.addActionListener(_ -> bookRoom());
        panel.add(btnBookRoom, gbc);
    }

    private void bookRoom() {
        String guestName = guestNameField.getText();
        String phone = phoneField.getText();
        int roomNumber = Integer.parseInt(roomNumberField.getText());
        //String roomType = (String) roomTypeComboBox.getSelectedItem();

        if (guestName.isEmpty() || phone.isEmpty() || roomNumber <= 0 || roomNumber > 60) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields! or provide a valid room number", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int roomPrice;
        String roomType = "";
        if(roomNumber >= 1 && roomNumber <= 20){
            roomPrice = 5000;
            roomType = "Small";
        }else if(roomNumber >= 21 && roomNumber <= 40){
            roomPrice = 10000;
            roomType = "Medium";
        }else{
            roomPrice = 20000;
            roomType = "Large";
        }

        try (Connection conn = DatabaseConnector.getConnection()) {
            String checkRoomQuery = "SELECT status FROM Rooms WHERE room_number = ?";
            try (PreparedStatement pst = conn.prepareStatement(checkRoomQuery)) {
                pst.setInt(1, roomNumber);
                ResultSet rs = pst.executeQuery();
                if (rs.next() && "occupied".equals(rs.getString("status"))) {
                    JOptionPane.showMessageDialog(frame, "This room is already occupied!", "Room Unavailable", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int guestId = getOrInsertGuest(conn, guestName, phone);
            int roomId = getOrInsertRoom(conn, roomNumber, roomType, roomPrice);

            String insertBookingQuery = "INSERT INTO Bookings (guest_id, room_id, start_time, end_time, total_amount_paid) " +
                    "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 3 HOUR), ?)";
            try (PreparedStatement pst = conn.prepareStatement(insertBookingQuery)) {
                pst.setInt(1, guestId);
                pst.setInt(2, roomId);
                pst.setInt(3, roomPrice);
                pst.executeUpdate();
                updateRoomStatus(conn, roomId);
                JOptionPane.showMessageDialog(frame, "Room booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getOrInsertGuest(Connection conn, String guestName, String phone) throws SQLException {
        String guestCheckQuery = "SELECT guest_id FROM Guests WHERE full_name = ? AND phone = ?";
        try (PreparedStatement pst = conn.prepareStatement(guestCheckQuery)) {
            pst.setString(1, guestName);
            pst.setString(2, phone);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("guest_id");
        }

        String insertGuestQuery = "INSERT INTO Guests (full_name, phone) VALUES (?, ?)";
        try (PreparedStatement insertGuest = conn.prepareStatement(insertGuestQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertGuest.setString(1, guestName);
            insertGuest.setString(2, phone);
            insertGuest.executeUpdate();
            ResultSet generatedKeys = insertGuest.getGeneratedKeys();
            if (generatedKeys.next()) return generatedKeys.getInt(1);
        }
        return -1;
    }

    private int getOrInsertRoom(Connection conn, int roomNumber, String roomType, int roomPrice) throws SQLException {
        String roomCheckQuery = "SELECT room_id FROM Rooms WHERE room_number = ?";
        try (PreparedStatement pst = conn.prepareStatement(roomCheckQuery)) {
            pst.setInt(1, roomNumber);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("room_id");
        }

        String insertRoomQuery = "INSERT INTO Rooms (room_number, room_type, price_per_3hrs, status) VALUES (?, ?, ?, 'available')";
        try (PreparedStatement insertRoom = conn.prepareStatement(insertRoomQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertRoom.setInt(1, roomNumber);
            insertRoom.setString(2, roomType);
            insertRoom.setInt(3, roomPrice);
            insertRoom.executeUpdate();
            ResultSet generatedKeys = insertRoom.getGeneratedKeys();
            if (generatedKeys.next()) return generatedKeys.getInt(1);
        }
        return -1;
    }

    private void updateRoomStatus(Connection conn, int roomId) throws SQLException {
        String updateRoomStatusQuery = "UPDATE Rooms SET status = 'occupied' WHERE room_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(updateRoomStatusQuery)) {
            pst.setInt(1, roomId);
            pst.executeUpdate();
        }
    }

    
    public void showWindow() {
        frame.setVisible(true);
    }
}
