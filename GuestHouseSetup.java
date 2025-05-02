import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class GuestHouseSetup {
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        String sql = """
                CREATE DATABASE IF NOT EXISTS isaac_guesthouse;
                USE isaac_guesthouse;

                CREATE TABLE IF NOT EXISTS Guests (
                    guest_id INT AUTO_INCREMENT PRIMARY KEY,
                    full_name VARCHAR(100) NOT NULL,
                    phone VARCHAR(15) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS Rooms (
                    room_id INT AUTO_INCREMENT PRIMARY KEY,
                    room_number VARCHAR(20) NOT NULL UNIQUE,
                    room_type VARCHAR(20) NOT NULL,
                    price_per_3hrs INT NOT NULL,
                    status VARCHAR(20) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS Bookings (
                    booking_id INT AUTO_INCREMENT PRIMARY KEY,
                    guest_id INT,
                    room_id INT,
                    start_time VARCHAR(20) NOT NULL,
                    end_time VARCHAR(20) NOT NULL,
                    total_amount_paid INT NOT NULL,
                    FOREIGN KEY (guest_id) REFERENCES Guests(guest_id),
                    FOREIGN KEY (room_id) REFERENCES Rooms(room_id)
                );

                CREATE TABLE IF NOT EXISTS workers (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100),
                    role VARCHAR(50),
                    phone VARCHAR(20),
                    address VARCHAR(255),
                    salary DOUBLE
                );

                INSERT INTO Rooms (room_number, room_type, price_per_3hrs, status) VALUES
                ('1', 'Small', 5000, 'available'), ('2', 'Small', 5000, 'available'), 
                ('3', 'Small', 5000, 'available'), ('4', 'Small', 5000, 'available'),
                ('5', 'Small', 5000, 'available'), ('6', 'Small', 5000, 'available'),
                ('7', 'Small', 5000, 'available'), ('8', 'Small', 5000, 'available'),
                ('9', 'Small', 5000, 'available'), ('10', 'Small', 5000, 'available'),
                ('11', 'Small', 5000, 'available'), ('12', 'Small', 5000, 'available'),
                ('13', 'Small', 5000, 'available'), ('14', 'Small', 5000, 'available'),
                ('15', 'Small', 5000, 'available'), ('16', 'Small', 5000, 'available'),
                ('17', 'Small', 5000, 'available'), ('18', 'Small', 5000, 'available'),
                ('19', 'Small', 5000, 'available'), ('20', 'Small', 5000, 'available'),
                ('21', 'Medium', 10000, 'available'), ('22', 'Medium', 10000, 'available'),
                ('23', 'Medium', 10000, 'available'), ('24', 'Medium', 10000, 'available'),
                ('25', 'Medium', 10000, 'available'), ('26', 'Medium', 10000, 'available'),
                ('27', 'Medium', 10000, 'available'), ('28', 'Medium', 10000, 'available'),
                ('29', 'Medium', 10000, 'available'), ('30', 'Medium', 10000, 'available'),
                ('31', 'Medium', 10000, 'available'), ('32', 'Medium', 10000, 'available'),
                ('33', 'Medium', 10000, 'available'), ('34', 'Medium', 10000, 'available'),
                ('35', 'Medium', 10000, 'available'), ('36', 'Medium', 10000, 'available'),
                ('37', 'Medium', 10000, 'available'), ('38', 'Medium', 10000, 'available'),
                ('39', 'Medium', 10000, 'available'), ('40', 'Medium', 10000, 'available'),
                ('41', 'Large', 20000, 'available'), ('42', 'Large', 20000, 'available'),
                ('43', 'Large', 20000, 'available'), ('44', 'Large', 20000, 'available'),
                ('45', 'Large', 20000, 'available'), ('46', 'Large', 20000, 'available'),
                ('47', 'Large', 20000, 'available'), ('48', 'Large', 20000, 'available'),
                ('49', 'Large', 20000, 'available'), ('50', 'Large', 20000, 'available'),
                ('51', 'Large', 20000, 'available'), ('52', 'Large', 20000, 'available'),
                ('53', 'Large', 20000, 'available'), ('54', 'Large', 20000, 'available'),
                ('55', 'Large', 20000, 'available'), ('56', 'Large', 20000, 'available'),
                ('57', 'Large', 20000, 'available'), ('58', 'Large', 20000, 'available'),
                ('59', 'Large', 20000, 'available'), ('60', 'Large', 20000, 'available');
                """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            for (String s : sql.split(";")) {
                if (!s.trim().isEmpty()) {
                    stmt.execute(s.trim() + ";");
                }
            }

            System.out.println("Database and tables created successfully, data inserted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
