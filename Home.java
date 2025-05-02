import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.Border;

public class Home {
    public static void display() {
     
        JButton btnBook = new JButton("BOOKING A ROOM");
        btnBook.setBackground(new Color(0,123,255));
        btnBook.setForeground(Color.white);
        btnBook.setOpaque(true);
        btnBook.setFont(new Font("Arial", Font.BOLD , 19));
        btnBook.setFocusPainted(false);

        JButton btnChangeRoomStatus = new JButton("CHANGE STATUS");
        btnChangeRoomStatus.setBackground(new Color(0,123,255));
        btnChangeRoomStatus.setForeground(Color.white);
        btnChangeRoomStatus.setFont(new Font("Arial", Font.BOLD, 19));
        btnChangeRoomStatus.setFocusPainted(false);

        JButton btnCheckRoomStatus = new JButton("CHECK STATUS");
        btnCheckRoomStatus.setBackground(new Color(0,123,255));
        btnCheckRoomStatus.setForeground(Color.white);
        btnCheckRoomStatus.setFont(new Font("Arial", Font.BOLD, 19));
        btnCheckRoomStatus.setFocusPainted(false);

        JButton btnBookHistory = new JButton("BOOKING HISTORY");
        btnBookHistory.setBackground(new Color(0,123,255));
        btnBookHistory.setForeground(Color.white);
        btnBookHistory.setFont(new Font("Arial", Font.BOLD, 19));
        btnBookHistory.setFocusPainted(false);

        JButton btnViewGuests = new JButton("ALL GUESTS");
        btnViewGuests.setBackground(new Color(0,123,255));
        btnViewGuests.setForeground(Color.white);
        btnViewGuests.setFont(new Font("Arial", Font.BOLD, 19));
        btnViewGuests.setFocusPainted(false);

        JButton btnViewRooms = new JButton("ALL ROOMS");
        btnViewRooms.setBackground(new Color(0,123,255));
        btnViewRooms.setForeground(Color.white);
        btnViewRooms.setFont(new Font("Arial", Font.BOLD, 19));
        btnViewRooms.setFocusPainted(false);

        JButton btnMoney = new JButton("MONEY PER MONTH");
        btnMoney.setBackground(new Color(0,123,255));
        btnMoney.setForeground(Color.white);
        btnMoney.setFont(new Font("Arial", Font.BOLD, 19));
        btnMoney.setFocusPainted(false);

        JButton btnStatistics = new JButton("STATISTICS");
        btnStatistics.setBackground(new Color(0,123,255));
        btnStatistics.setForeground(Color.white);
        btnStatistics.setFont(new Font("Arial", Font.BOLD, 19));
        btnStatistics.setFocusPainted(false);

        btnBook.setBounds(0, 0, 300, 87);
        btnChangeRoomStatus.setBounds(0, 87, 300, 87);
        btnCheckRoomStatus.setBounds(0, 174, 300, 87);
        btnBookHistory.setBounds(0, 261, 300, 87);
        btnViewGuests.setBounds(0, 348, 300, 87);
        btnViewRooms.setBounds(0, 435, 300, 87);
        btnMoney.setBounds(0, 522, 300, 87);
        btnStatistics.setBounds(0, 609, 300, 87);

        JPanel sidebar = new JPanel();
        sidebar.setBounds(0, 0, 300, 700);
        sidebar.setBackground(new Color(176,196,222));
        sidebar.setLayout(null);
        sidebar.add(btnBook);
        sidebar.add(btnChangeRoomStatus);
        sidebar.add(btnCheckRoomStatus);
        sidebar.add(btnBookHistory);
        sidebar.add(btnViewGuests);
        sidebar.add(btnViewRooms);
        sidebar.add(btnMoney);
        sidebar.add(btnStatistics);

        JButton logout = new JButton("LOGOUT");
        logout.setBounds(780, 110, 100, 50);
        logout.setFocusPainted(false);
        logout.setOpaque(false);
        logout.setContentAreaFilled(false);
        logout.setForeground(Color.white);

        JPanel heading = new JPanel();
        JLabel headinglabel = new JLabel("ISAAC GUESTHOUSE MANAGEMENT SYSTEM");
        JLabel adminlabel = new JLabel("ADMIN DASHBOARD");
        adminlabel.setFont(new Font("Arial", Font.BOLD, 48));
        adminlabel.setForeground(Color.white);
        headinglabel.setFont(new Font("Arial", Font.BOLD, 38));
        headinglabel.setForeground(new Color(255,255,255));
        headinglabel.setBounds(15, 0, 900, 100);
        adminlabel.setBounds(110, 90, 600, 90);
        Border lineBorder = BorderFactory.createLineBorder(Color.blue, 1);
        heading.setBorder(lineBorder);
        heading.setBounds(300, 0, 900, 200);
        heading.setBackground(new Color(0,123,255));
        heading.setLayout(null);
        heading.add(headinglabel);
        heading.add(adminlabel);

        JButton workers = new JButton("WORKERS");
        workers.setBounds(670, 110, 100, 50);
        workers.setFocusPainted(false);
        workers.setOpaque(false);
        workers.setContentAreaFilled(false);
        workers.setForeground(Color.white);

        heading.add(logout);
        heading.add(workers);

        JLabel totalrooms = new JLabel("TOTAL ROOMS: 60");
        totalrooms.setBounds(0, 0, 380, 70);
        totalrooms.setFont(new Font("Arial", Font.BOLD, 34));
        totalrooms.setForeground(new Color(51,51,51));

        JLabel availableRooms = new JLabel("AVAILABLE: 20");
        availableRooms.setBounds(0, 220, 390, 70);
        availableRooms.setFont(new Font("Arial", Font.BOLD, 34));
        availableRooms.setForeground(new Color(51,51,51));

        JLabel occupiedRooms = new JLabel("OCCUPIED: 20");
        occupiedRooms.setBounds(0, 280, 350, 70);
        occupiedRooms.setFont(new Font("Arial", Font.BOLD, 34));
        occupiedRooms.setForeground(new Color(51,51,51));

        JLabel moneyOfTheDay = new JLabel("TODAY: MK" + getTotalMoneyForToday());
        moneyOfTheDay.setBounds(0, 350, 450, 70);
        moneyOfTheDay.setFont(new Font("Arial", Font.BOLD, 34));
        moneyOfTheDay.setForeground(new Color(51,51,51));

        JPanel home = new JPanel();
        home.add(availableRooms);
        home.add(occupiedRooms);
        home.add(moneyOfTheDay);
        home.setBounds(300, 200, 900, 530);
        home.setBackground(new Color(255,255,255));
        home.setLayout(null);
        home.add(totalrooms);
        JLabel price1 = new JLabel("ROOM 1-20  : MK5000");
        price1.setFont(new Font("Arial",1,34));
        price1.setBounds(0,50,400,50);
        price1.setForeground(new Color(51,51,51));
        home.add(price1);

        JLabel price2 = new JLabel("ROOM 21-40: MK10000");
        price2.setFont(new Font("Arial",1,34));
        price2.setBounds(0,100,400,50);
        price2.setForeground(new Color(51,51,51));
        home.add(price2);

        JLabel price3 = new JLabel("ROOM 41-60: MK20000");
        price3.setFont(new Font("Arial",1,34));
        price3.setBounds(0,150,400,50);
        price3.setForeground(new Color(51,51,51));
        home.add(price3);

        PieChartAWT chart = new PieChartAWT();
        chart.setBounds(400, 0, 500, 500);
        chart.setBackground(new Color(255,255,255));
        chart.setBorder(lineBorder);
        home.add(chart);
        //home.setBorder(lineBorder);

        JFrame frame = new JFrame("ISAAC GUESTHOUSE");
        frame.setSize(1200, 730);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setUndecorated(false);
        //frame.setShape(new RoundRectangle2D.Double(0,0,1200,700,50,20));
        frame.add(sidebar);
        frame.add(heading);
        frame.add(home);
        frame.setVisible(true);


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> refreshUI(availableRooms, occupiedRooms, moneyOfTheDay, chart));
            }
        }, 0, 5000);

        
        btnBook.addActionListener(_ -> new BookApp().showWindow());
        btnChangeRoomStatus.addActionListener(_ -> new ChangeRoomStatusApp().showWindow());
        btnCheckRoomStatus.addActionListener(_ -> new CheckRoomStatusApp().showWindow());
        btnBookHistory.addActionListener(_ -> new ViewBookingHistoryApp().showWindow());
        btnViewGuests.addActionListener(_ -> new ViewGuestsApp().showWindow());
        btnViewRooms.addActionListener(_ -> new ViewRoomsApp().showWindow());
        btnMoney.addActionListener(_ -> new ViewTotalMoneyApp().showWindow());
        btnStatistics.addActionListener(_ -> new ViewStatisticsApp().showWindow());

        logout.addActionListener(_ -> {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?");
            if (response == 0) {
                System.exit(1);
            }
        });

        workers.addActionListener(_ ->{
            WorkerManager.view();
        });
    }

    private static void refreshUI(JLabel availableRooms, JLabel occupiedRooms, JLabel moneyOfTheDay, PieChartAWT chart) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String availableQuery = "SELECT COUNT(*) FROM rooms WHERE status = 'available'";
            PreparedStatement availableStmt = connection.prepareStatement(availableQuery);
            ResultSet availableResult = availableStmt.executeQuery();
            if (availableResult.next()) {
                int availableCount = availableResult.getInt(1);
                availableRooms.setText("AVAILABLE: " + availableCount);
            }

            String occupiedQuery = "SELECT COUNT(*) FROM rooms WHERE status = 'occupied'";
            PreparedStatement occupiedStmt = connection.prepareStatement(occupiedQuery);
            ResultSet occupiedResult = occupiedStmt.executeQuery();
            if (occupiedResult.next()) {
                int occupiedCount = occupiedResult.getInt(1);
                occupiedRooms.setText("OCCUPIED: " + occupiedCount);
            }

            double totalMoney = getTotalMoneyForToday();
            moneyOfTheDay.setText("TODAY: MK" + totalMoney);

            chart.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getTotalMoneyForToday() {
        double totalMoney = 0;

        LocalDate currentDate = LocalDate.now();
        String today = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String sql = "SELECT SUM(total_amount_paid) AS total_today FROM bookings WHERE DATE(start_time) = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, today);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalMoney = resultSet.getDouble("total_today");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalMoney;
    }


}
