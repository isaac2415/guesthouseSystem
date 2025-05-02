import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class PieChartAWT extends JPanel {

    private int[] values = {20, 20}; 
    private final Color[] colors = {new Color(70,130,180), new Color(0,191,255)};
    private final String[] labels = {"OCCUPIED", "AVAILABLE"};

    public PieChartAWT() {
        fetchRoomData();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchRoomData();
                repaint();
                Home.getTotalMoneyForToday();
            }
        }, 5000, 5000);
    }

    private void fetchRoomData() {
        try (Connection connection = DatabaseConnector.getConnection()) {
        
            String availableQuery = "SELECT COUNT(*) FROM rooms WHERE status = 'available'";
            PreparedStatement availableStmt = connection.prepareStatement(availableQuery);
            ResultSet availableResult = availableStmt.executeQuery();
            if (availableResult.next()) {
                values[1] = availableResult.getInt(1); 
            }

            String occupiedQuery = "SELECT COUNT(*) FROM rooms WHERE status = 'occupied'";
            PreparedStatement occupiedStmt = connection.prepareStatement(occupiedQuery);
            ResultSet occupiedResult = occupiedStmt.executeQuery();
            if (occupiedResult.next()) {
                values[0] = occupiedResult.getInt(1); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        int total = values[0] + values[1];

        if (total == 0) return; 

        int startAngle = 0;
        int x = 50, y = 50, width = 400, height = 400;

        for (int i = 0; i < values.length; i++) {
            int arcAngle = (int) Math.round((values[i] * 360.0) / total);
            g2d.setColor(colors[i]);
            g2d.fillArc(x, y, width, height, startAngle, arcAngle);

            int midAngle = startAngle + arcAngle / 2;
            int labelX = (int) (x + width / 2 + (width / 3) * Math.cos(Math.toRadians(midAngle)));
            int labelY = (int) (y + height / 2 - (height / 3) * Math.sin(Math.toRadians(midAngle)));

            g2d.setColor(Color.BLACK);
            g2d.drawString(labels[i], labelX + 2, labelY + 2); 
            g2d.setColor(Color.WHITE);
            g2d.drawString(labels[i], labelX, labelY);

            startAngle += arcAngle;
        }
    }
}
