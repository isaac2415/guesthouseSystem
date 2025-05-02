import javax.swing.*;
import java.awt.*;

public class MainPage {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ISAAC GUESTHOUSE MANAGEMENT SYSTEM");

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); 

            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setLayout(new BorderLayout(20, 20));


            JLabel header = new JLabel("WELCOME TO ISAAC GUESTHOUSE MANAGEMENT SYSTEM", SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 36));
            header.setForeground(Color.BLUE);
            header.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
            panel.add(header, BorderLayout.NORTH);

            JLabel description = new JLabel("<html><center>The complete solution for managing guesthouse bookings, room statuses, and guest information.<br>Explore the features below to get started.</center></html>", SwingConstants.CENTER);
            description.setFont(new Font("Arial", Font.PLAIN, 16));
            description.setForeground(Color.GRAY);
            description.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); 
            panel.add(description, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(4, 2, 20, 20)); 
            buttonPanel.setBackground(Color.WHITE);

            
            JButton btnChangeRoomStatus = createButton("CHANGE ROOM TO AVAILABLE STATUS");
            JButton btnCheckRoomStatus = createButton("CHECK ROOM STATUS");
            JButton btnGuesthouseManagement = createButton("BOOKING A ROOM");
            JButton btnViewBookingHistory = createButton("VIEW BOOKING HISTORY");
            JButton btnViewGuests = createButton("VIEW ALL GUESTS");
            JButton btnViewRooms = createButton("VIEW ALL ROOMS");
            JButton btnViewTotalMoney = createButton("VIEW TOTAL MONEY");
            JButton btnStatistics = createButton("STATISTICS");

            btnChangeRoomStatus.addActionListener(_ -> new ChangeRoomStatusApp().showWindow());
            btnCheckRoomStatus.addActionListener(_ -> new CheckRoomStatusApp().showWindow());
            btnGuesthouseManagement.addActionListener(_ -> new BookApp().showWindow());
            btnViewBookingHistory.addActionListener(_ -> new ViewBookingHistoryApp().showWindow());
            btnViewGuests.addActionListener(_ -> new ViewGuestsApp().showWindow());
            btnViewRooms.addActionListener(_ -> new ViewRoomsApp().showWindow());
            btnViewTotalMoney.addActionListener(_ -> new ViewTotalMoneyApp().showWindow());
            btnStatistics.addActionListener(_ -> new ViewStatisticsApp().showWindow());

            buttonPanel.add(btnChangeRoomStatus);
            buttonPanel.add(btnCheckRoomStatus);
            buttonPanel.add(btnGuesthouseManagement);
            buttonPanel.add(btnViewBookingHistory);
            buttonPanel.add(btnViewGuests);
            buttonPanel.add(btnViewRooms);
            buttonPanel.add(btnViewTotalMoney);
            buttonPanel.add(btnStatistics);

            panel.add(buttonPanel, BorderLayout.SOUTH);

            frame.add(panel);
            frame.setVisible(true);
        });
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 123, 255)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); 
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, button.getPreferredSize().height * 5));

        return button;
    }
}
