package hospital_dbms;
import hospital_dbms.ui.HospitalUI;
import java.sql.*;

/**
 * Main class for the HospitalDBMS program. Starts the connection to the database and initiates the UI
 */
public class HospitalDBMS {

    // Username to log in to the Database, stored in the Environment variable ${JDBC_USERNAME}
    private static final String USERNAME = "root"; //System.getenv("JDBC_USERNAME");
    // Password to log in to the Database, stored in the Environment variable ${JDBC_PASSWORD}
    private static final String PASSWORD = "changeme"; //System.getenv("JDBC_PASSWORD");
    // The URL of the Database
    private static final String JDBC_URL = "jdbc:mysql://localhost:1111/db";
    // Connection object
    private static Connection connection = null;

    /**
     * Main method for the program. Initiates the SQL connection and runs the UI
     * @param args Arguments for the program
     */
    public static void main(String[] args) throws Exception {
        Connection connection = getConnection();
        new HospitalUI(connection);
    }

    /**
     * Returns the SQL connection object
     */
    public static Connection getConnection() {

        if (connection == null) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Unable to load jdbc driver");
            }
            try {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return connection;
    }

}
