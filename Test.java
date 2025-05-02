import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class Test extends JFrame {

    private JLabel availableRooms;
    private JLabel occupiedRooms;
    private JLabel moneyOfTheDay;
    private PieChartAWT pieChartAWT;

    public Test() {
        
        availableRooms = new JLabel("AVAILABLE: " + getAvailableRoomsCount());
        availableRooms.setBounds(0, 70, 390, 70);
        availableRooms.setFont(new Font("Arial", Font.BOLD, 34));
        availableRooms.setForeground(Color.GREEN);

        occupiedRooms = new JLabel("OCCUPIED: " + getOccupiedRoomsCount());
        occupiedRooms.setBounds(0, 190, 350, 70);
        occupiedRooms.setFont(new Font("Arial", Font.BOLD, 34));
        occupiedRooms.setForeground(Color.RED);

        moneyOfTheDay = new JLabel("TODAY: MK" + getTotalMoneyForToday());
        moneyOfTheDay.setBounds(0, 290, 450, 70);
        moneyOfTheDay.setFont(new Font("Arial", Font.BOLD, 34));
        moneyOfTheDay.setForeground(Color.GREEN);

        pieChartAWT = new PieChartAWT();
        pieChartAWT.setBounds(500, 50, 500, 500);

        // Set up frame properties
        setLayout(null);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Add components to the frame
        add(availableRooms);
        add(occupiedRooms);
        add(moneyOfTheDay);
        add(pieChartAWT);

        // Set up the timer to refresh every 5 seconds
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the displayed information every 5 seconds
                SwingUtilities.invokeLater(() -> refreshFrame());
            }
        }, 0, 5000); // Initial delay: 0ms, repeat every 5 seconds
    }

    // Method to refresh the entire JFrame
    private void refreshFrame() {
        // Refresh the labels with updated values
        availableRooms.setText("AVAILABLE: " + getAvailableRoomsCount());
        occupiedRooms.setText("OCCUPIED: " + getOccupiedRoomsCount());
        moneyOfTheDay.setText("TODAY: MK" + getTotalMoneyForToday());

        // Refresh the pie chart with updated data
        pieChartAWT.repaint();

        // Revalidate and repaint the frame to reflect the changes
        revalidate();
        repaint();
    }

    // Method to retrieve the count of available rooms
    private int getAvailableRoomsCount() {
        int count = 0;
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT COUNT(*) FROM rooms WHERE status = 'available'";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Method to retrieve the count of occupied rooms
    private int getOccupiedRoomsCount() {
        int count = 0;
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT COUNT(*) FROM rooms WHERE status = 'occupied'";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Method to retrieve the total money made today
    private double getTotalMoneyForToday() {
        double totalMoney = 0;
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT SUM(total_amount_paid) FROM bookings WHERE DATE(start_time) = CURDATE()";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                totalMoney = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMoney;
    }

    // Main method to launch the frame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
