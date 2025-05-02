import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewStatisticsApp {
    private JFrame frame;
    private JPanel statsPanel;
    private JButton btnStatistics;
    private int[] bookingsPerDay;
    private String[] bookingDates;

    public ViewStatisticsApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Booking Statistics");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        btnStatistics = new JButton("SHOW STATISTICS");
        btnStatistics.setFont(new Font("Arial", Font.BOLD, 16));
        btnStatistics.setBackground(new Color(0, 102, 204));
        btnStatistics.setForeground(Color.WHITE);
        btnStatistics.setFocusPainted(false);
        btnStatistics.addActionListener(_ -> showStatistics());

        frame.add(btnStatistics, BorderLayout.NORTH);

        statsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bookingsPerDay != null) {
                    drawBarChart(g);
                }
            }
        };
        statsPanel.setBackground(Color.WHITE);
        frame.add(statsPanel, BorderLayout.CENTER);
    }

    private void fetchBookingData() {
        bookingsPerDay = new int[7];
        bookingDates = new String[7];
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT COUNT(*) AS bookings, DATE(start_time) AS booking_date " +
                           "FROM Bookings WHERE start_time >= CURDATE() - INTERVAL 7 DAY " +
                           "GROUP BY booking_date ORDER BY booking_date";
            try (PreparedStatement stmt = conn.prepareStatement(query); 
                 ResultSet rs = stmt.executeQuery()) {
                int day = 0;
                while (rs.next()) {
                    bookingsPerDay[day] = rs.getInt("bookings");
                    bookingDates[day] = rs.getString("booking_date"); 
                    day++;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawBarChart(Graphics g) {
        int barWidth = 50;
        int gap = 20;
        int maxHeight = 300;
        int maxBookings = getMaxBookings();

        double scale = maxBookings > 0 ? (double) maxHeight / maxBookings : 1;

        g.setColor(new Color(0, 102, 204));
        for (int i = 0; i < 7; i++) {
            int barHeight = (int) (bookingsPerDay[i] * scale);
            g.fillRect(100 + i * (barWidth + gap), maxHeight - barHeight, barWidth, barHeight);
        }

        g.setColor(Color.BLACK);
        //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); 

        for (int i = 0; i < 7; i++) {
            String dateLabel = bookingDates[i] != null ? bookingDates[i] : getDateForPastDay(i);
            g.drawString(dateLabel, 100 + i * (barWidth + gap), maxHeight + 20);
        }
    }

    private String getDateForPastDay(int daysAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo); 
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }

    private int getMaxBookings() {
        int max = 0;
        for (int booking : bookingsPerDay) {
            if (booking > max) {
                max = booking;
            }
        }
        return max;
    }

    private void showStatistics() {
        fetchBookingData();
        statsPanel.repaint();
    }

    public void showWindow() {
        frame.setVisible(true);
    }
}
