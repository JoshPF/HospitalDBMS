package hospital_dbms.models;

import java.sql.*;

import hospital_dbms.models.Relation;
import static hospital_dbms.HospitalDBMS.getConnection;

/**
 * Relates HospitalVisits to Staff with a many-to-many relationship
 * Staff (nurses or Doctors) may be responsible for 0..* patients visiting
 * A visit may have 0..* people responsible for it
 */
public class ResponsibleStaff extends Relation {

    // The HospitalVisit the Staff member is the ResponsibleStaff for
    private int visitID;
    // The Staff member's ID number
    private int staffID;

    /** Construct ResponsibleStaff from properties */
    public ResponsibleStaff(int visitID, int staffID) {
        setVisitID(visitID);
        setStaffID(staffID);
    }

    /** CREATE the ResponsibleStaff table if it doesn't exist */
    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS ResponsibleStaff ( " +
                "visitID int NOT NULL, " +
                "staffID int NOT NULL, " +
                "CONSTRAINT fk_rs_hospitalVisitID " +
                "    FOREIGN KEY (visitID) REFERENCES HospitalVisit(ID) " +
                "    ON DELETE CASCADE " +
                "    ON UPDATE CASCADE, " +
                "CONSTRAINT fk_responsible_staffID " +
                "    FOREIGN KEY (staffID) REFERENCES Staff(personID) " +
                "    ON DELETE CASCADE " +
                "    ON UPDATE CASCADE " +
                ");");
    }

    /** DROP the ResponsibleStaff table if it does exist */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS ResponsibleStaff;");

    }

    /** INSERT the current object into the database */
    public void insert() throws SQLException {
        String query = "INSERT INTO ResponsibleStaff" +
                       "(visitID, staffID)" +
                       "VALUES ( ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, getVisitID());
        statement.setInt(2, getStaffID());

        statement.executeUpdate();
    }

    /** Update the DB with the current object (not supported for this relation) */
    public void update() throws SQLException {
        throw new UnsupportedOperationException("Cannot update a 2-way relation");
    }

    /**
     * Get visitID.
     *
     * @return visitID as int.
     */
    public int getVisitID() {
        return visitID;
    }

    /**
     * Set visitID.
     *
     * @param visitID the value to set.
     */
    public void setVisitID(int visitID) {
        this.visitID = visitID;
    }

    /**
     * Get staffID.
     *
     * @return staffID as int.
     */
    public int getStaffID() {
        return staffID;
    }

    /**
     * Set staffID.
     *
     * @param staffID the value to set.
     */
    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }
}
