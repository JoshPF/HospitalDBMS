package hospital_dbms.models;

import java.sql.*;

import hospital_dbms.models.Relation;
import static hospital_dbms.HospitalDBMS.getConnection;

public class Bed extends Relation {

    private int ID;
    private int hospitalID;
    private String specializationName;
    private Integer nurseID;
    private boolean occupied;

    /**
     * Construct a bed object from its properties
     */
    public Bed(int hospitalID, String specializationName, Integer nurseID, boolean occupied) {
        setHospitalID(hospitalID);
        setSpecializationName(specializationName);
        setNurseID(nurseID);
        setOccupied(occupied);
    }

    /**
     * Run a CREATE TABLE to make the Bed table if it doesn't exist
     */
    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Bed ( " +
                "ID INT NOT NULL AUTO_INCREMENT, " +
                "hospitalID INT NOT NULL, " +
                "specializationName VARCHAR(255), " +
                "nurseID INT, " +
                "occupied BOOLEAN DEFAULT FALSE, " +
                "PRIMARY KEY (ID), " +
                "CONSTRAINT fk_bed_hospitalID " +
                "    FOREIGN KEY (hospitalID) REFERENCES Hospital(ID) " +
                "    ON DELETE CASCADE " +
                "    ON UPDATE CASCADE, " +
                "CONSTRAINT fk_bed_specialization " +
                "    FOREIGN KEY (specializationName) REFERENCES Specialization(name) " +
                "    ON DELETE CASCADE " +
                "    ON UPDATE CASCADE " +
                ");");
    }

    /**
     * Execute a DROP TABLE if the table exists
     */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS Bed;");
    }

    /**
     * Lookup a Bed object with the given id and return it
     */
    public static Bed getById(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM Bed WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.absolute(1);

        int storedID = rs.getInt("ID");
        int storedHospitalID = rs.getInt("hospitalID");
        String storedSpecialization = rs.getString("specializationName");
        int storedNurseID = rs.getInt("nurseID");
        boolean storedOccupied = rs.getBoolean("occupied");
        rs.close();

        Bed b = new Bed(storedHospitalID, storedSpecialization, storedNurseID, storedOccupied);
        b.setID(storedID);
        return b;
    }
    /**
 * Retreives available beds given a hospitalID and specialization
 */
    public static Bed getAvailableBed(int hospitalID, String specialization) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM Bed WHERE hospitalID = ? AND specializationName=? AND occupied=FALSE LIMIT 1";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, hospitalID);
        statement.setString(2, specialization);

        ResultSet rs = statement.executeQuery();

        if (!rs.next()) {
            return null;
        }
        rs.absolute(1);

        int storedID = rs.getInt("ID");
        int storedHospitalID = rs.getInt("hospitalID");
        String storedSpecialization = rs.getString("specializationName");
        int storedNurseID = rs.getInt("nurseID");
        boolean storedOccupied = rs.getBoolean("occupied");
        rs.close();

        Bed b = new Bed(storedHospitalID, storedSpecialization, storedNurseID, storedOccupied);
        b.setID(storedID);
        return b;
    }

    /**
     * Insert the current object into the database
     */
    public void insert() throws SQLException {
        String query = "INSERT INTO Bed" +
                       "(hospitalID, specializationName, nurseID, occupied)" +
                       "VALUES ( ?, ?, ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, getHospitalID());
        if (getSpecializationName() == null) {
            statement.setNull(2, Types.VARCHAR);
        } else {
            statement.setString(2, getSpecializationName());
        }
        if (getNurseID() == null) {
            statement.setNull(3, Types.INTEGER);
        } else {
            statement.setInt(3, getNurseID());
        }
        statement.setBoolean(4, isOccupied());

        statement.executeUpdate();

        // Get the id that the database created
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.ID = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating Bed failed, no ID obtained.");
            }
        }

    }

    /**
     * Update the object in the databse to match the Java object
     */
    public void update() throws SQLException {
        String query = "UPDATE Bed set hospitalID=?, specializationName=?, nurseID=?, occupied=? WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, getHospitalID());
        if (getSpecializationName() == null) {
            statement.setNull(2, Types.VARCHAR);
        } else {
            statement.setString(2, getSpecializationName());
        }
        if (getNurseID() == null) {
            statement.setNull(3, Types.INTEGER);
        } else {
            statement.setInt(3, getNurseID());
        }
        statement.setBoolean(4, isOccupied());

        statement.setInt(5, getID());

        statement.executeUpdate();
    }

    /**
     * Delete the current object from the databse
     */
    public void delete() throws SQLException {
        String query = "DELETE FROM Bed WHERE ID = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, getID());
        statement.executeUpdate();
        connection.commit();
    }

    /** Set a bed as unoccupied and unset it from any HospitalVisit */
    public void release() throws SQLException {
        setOccupied(false);

        connection.setAutoCommit(false);
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE HospitalVisit SET bedNumber=NULL WHERE bedNumber=?;");
            statement.setInt(1, getID());
            statement.executeUpdate();

            this.update();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    /**
     * Get ID.
     *
     * @return ID as int.
     */
    public int getID() {
        return ID;
    }

    /**
     * Set ID.
     *
     * @param ID the value to set.
     */
    public void setID(int ID) {
        this.ID = ID;
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
     * Get specializationName.
     *
     * @return specializationName as String.
     */
    public String getSpecializationName() {
        return specializationName;
    }

    /**
     * Set specializationName.
     *
     * @param specializationName the value to set.
     */
    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    /**
     * Get nurseID.
     *
     * @return nurseID as int.
     */
    public Integer getNurseID() {
        return nurseID;
    }

    /**
     * Set nurseID.
     *
     * @param nurseID the value to set.
     */
    public void setNurseID(int nurseID) {
        this.nurseID = nurseID;
    }

    /**
     * Get occupied.
     *
     * @return occupied as boolean.
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Set occupied.
     *
     * @param occupied the value to set.
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
