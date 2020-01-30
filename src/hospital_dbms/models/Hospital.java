package hospital_dbms.models;

import static hospital_dbms.HospitalDBMS.getConnection;
import hospital_dbms.models.Relation;
import java.sql.*;

public class Hospital extends Relation {
    private int hospitalID;
    private Integer adminID;
    private String address;
    private String phone;

    //construct hospital object
    public Hospital(String address, String phone){
        this.address = address;
        this.phone = phone;
    }

    //called to create the table for Hospitals
    public static void createTable() throws SQLException{
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS Hospital ( " +
            "ID int NOT NULL AUTO_INCREMENT, " +
            "address VARCHAR(255) NOT NULL, " +
            "phoneNum CHAR(10) NOT NULL, " +
            "PRIMARY KEY (ID)" +
            ");"
        );
    }

    // Delete the entire database table
    public static void dropTable() throws SQLException {
        connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS Hospital;");
    }

    //Updates hospital object with new values
    public void update() throws SQLException {
        connection = getConnection();
        String query = "UPDATE Hospital set address=?, phoneNum=? WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, this.address);
        statement.setString(2, this.phone);
        statement.setInt(3, this.hospitalID);

        statement.executeUpdate();
    }

    //called to insert Hospital object into the table
    public void insert() throws SQLException {
        String query = "INSERT INTO Hospital" +
            "(address, phoneNum)" +
            "VALUES ( ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, this.address);
        statement.setString(2, this.phone);
        statement.executeUpdate();
        connection.commit();

        // Get the id that the database created
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.hospitalID = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating Hospital failed, no ID obtained.");
            }
        }
    }

    //returns a hospital based on a given ID
    public static Hospital getByID(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM Hospital WHERE ID = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.absolute(1);

        int storedID = rs.getInt("ID");
        String storedAddress = rs.getString("address");
        String storedPhone = rs.getString("phoneNum");

        rs.close();
        Hospital h = new Hospital(storedAddress, storedPhone);
        h.setHospitalID(storedID);
        return h;
    }
    /**
 * deletes a Hospital
 */
    public void delete() {
        String query = "DELETE FROM Hospital WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.hospitalID);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get hospitalID.
     *
     * @return hospitalID as int.
     */
    public int getHospitalID() {
        return hospitalID;
    }

    /**
     * Set hospitalID.
     *
     * @param hospitalID the value to set.
     */
    public void setHospitalID(int hospitalID) {
        this.hospitalID = hospitalID;
    }

    /**
     * Get address.
     *
     * @return address as String.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set address.
     *
     * @param address the value to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get phone.
     *
     * @return phone as String.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set phone.
     *
     * @param phone the value to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
