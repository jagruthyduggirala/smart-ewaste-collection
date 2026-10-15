import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/ewaste_db";

    private static final String USER = "root";

    public static Connection getConnection() {

        Connection con = null;

        try {
            String password =
                    System.getenv("EWASTE_DB_PASSWORD");

            if (password == null || password.isEmpty()) {
                throw new IllegalStateException(
                        "EWASTE_DB_PASSWORD is not configured."
                );
            }

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    URL, USER, password
            );

            System.out.println(
                    "Database connected successfully!"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}