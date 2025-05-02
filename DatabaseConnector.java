import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        String baseUrl = "jdbc:mysql://localhost:3306/";
        String dbName = "isaac_guesthouse";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(baseUrl, user, password);
             Statement stmt = conn.createStatement()) {

            ResultSet resultSet = stmt.executeQuery(
                "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + dbName + "'");

            if (!resultSet.next()) {
                System.out.println("Database not found. Setting up...");
                GuestHouseSetup.main(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while checking or creating database.", e);
        }

        return DriverManager.getConnection(baseUrl + dbName, user, password);
    }
}
