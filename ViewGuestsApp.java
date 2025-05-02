import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ViewGuestsApp {
    private final JFrame frame;
    private final JTable guestTable;

    public ViewGuestsApp() {
        frame = new JFrame("Guest List");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        guestTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Guest ID", "Full Name", "Phone"}));
        styleTable();
        displayGuests();

        JScrollPane scrollPane = new JScrollPane(guestTable);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void styleTable() {
        JTableHeader header = guestTable.getTableHeader();
        header.setBackground(new Color(0,123,255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        guestTable.setFont(new Font("Arial", Font.PLAIN, 14));
        guestTable.setRowHeight(20);
    }

    private void displayGuests() {
        DefaultTableModel model = (DefaultTableModel) guestTable.getModel();
        model.setRowCount(0); 

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT guest_id, full_name, phone FROM Guests")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("guest_id"), rs.getString("full_name").toUpperCase(), rs.getString("phone")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showWindow() {
        frame.setVisible(true);
    }
}
