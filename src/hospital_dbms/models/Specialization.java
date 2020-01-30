package hospital_dbms.models;

import java.sql.*;
import hospital_dbms.models.Relation;
import hospital_dbms.HospitalDBMS;

/**
 * Represents a possible specialization type
 * Hospitals can allocate beds to certain Specializations and Doctors can have a Specialization
 */
public class Specialization extends Relation {

    // The name of a Specialization
    private String name;
    // The number of charges per day
    private Float chargesPerDay;

    /** Construct a Specialization object from all properties */
    public Specialization(String name, Float chargesPerDay) {
        this.name = name;
        this.chargesPerDay = chargesPerDay;
    }

    /** CREATE the Specialization table if it doesn't exist */
    public static void createTable() throws SQLException{
        Connection connection = HospitalDBMS.getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Specialization (" + 
                "name VARCHAR(255) NOT NULL, " +
                "chargesPerDay FLOAT, " +
                "PRIMARY KEY (name)" +
                ");"
            );
        connection.commit();
    }

    /** DROP the Specialization table if it does exist */
    public static void dropTable() throws SQLException {
        Connection connection = HospitalDBMS.getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS Specialization;");
    }


    /** INSERT the current object into the database */
    public void insert() {
        String query = "INSERT INTO Specialization VALUES ( ?, ? )";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.name);
            if (getChargesPerDay() == null) {
                statement.setNull(2, Types.FLOAT);
            } else {
                statement.setFloat(2, getChargesPerDay());
            }
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error occurred while updating an entry in Staff.");
            try {
                if(connection!=null) {
                    connection.rollback();
                    System.out.println("Successfully rolled back changes during Staff update.");
                }
            } catch(SQLException e2) {
                System.out.println("Error occurred while rolling back an update in Staff: ");
                e2.printStackTrace();
            }
        }
    }

    /** Lookup a Specialization by name to get it as an object */
    public static Specialization getByName(String name) {
        String query = "SELECT * FROM Specialization WHERE name = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            rs.next();

            String storedName = rs.getString("name");
            Float storedChargesPerDay = rs.getFloat("chargesPerDay");
            rs.close();

            return new Specialization(storedName, storedChargesPerDay);

        } catch (SQLException e) {
            throw new RuntimeException("aaah");
        }
    }

    /** Update the database with the current Specialization object (NOT SUPPORTED) */
    public void update() {
        throw new UnsupportedOperationException("Cannot update a specialization (only one field)");
    }

    /** DELETE the current Specialization from the database */
    public void delete() {
        String query = "DELETE FROM Specialization WHERE name = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.name);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {

        }
    }

    /**
     * Get name.
     *
     * @return name as String.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name.
     *
     * @param name the value to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get chargesPerDay.
     *
     * @return chargesPerDay as Float.
     */
    public Float getChargesPerDay() {
        return chargesPerDay;
    }

    /**
     * Set chargesPerDay.
     *
     * @param chargesPerDay the value to set.
     */
    public void setChargesPerDay(Float chargesPerDay) {
        this.chargesPerDay = chargesPerDay;
    }
}
