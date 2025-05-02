import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class ViewRoomsApp {

    private JFrame frame;
    private JTable roomsTable;
    private JScrollPane scrollPane;
  
    public ViewRoomsApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Room List");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Room Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);

        roomsTable = new JTable();
        roomsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomsTable.setRowHeight(25);
        roomsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        roomsTable.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Room ID", "Room Number", "Room Type", "Price (3hrs)", "Status"}
        ));

        JTableHeader header = roomsTable.getTableHeader();
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);

        displayRooms();

        scrollPane = new JScrollPane(roomsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private void displayRooms() {
        DefaultTableModel model = (DefaultTableModel) roomsTable.getModel();

        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT room_id, room_number, room_type, price_per_3hrs, status FROM Rooms";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int roomId = rs.getInt("room_id");
                    int roomNumber = rs.getInt("room_number");
                    String roomType = rs.getString("room_type");
                    int pricePer3hrs = rs.getInt("price_per_3hrs");
                    String status = rs.getString("status");

                    model.addRow(new Object[] { roomId, roomNumber, roomType, pricePer3hrs, status });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        roomsTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    class StatusCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel(value.toString());
            if ("occupied".equalsIgnoreCase(value.toString())) {
                label.setForeground(Color.RED); 
            } else {
                label.setForeground(Color.BLACK); 
            }
            return label;
        }
    }
}
