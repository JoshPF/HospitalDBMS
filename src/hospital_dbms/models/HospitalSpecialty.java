package hospital_dbms.models;

import java.sql.*;

import hospital_dbms.models.Relation;
import static hospital_dbms.HospitalDBMS.getConnection;
/**
 * The relation that describes the relationship between Hospital and specialty
 */
public class HospitalSpecialty extends Relation {

    private int hospitalID;
    private String specialization;
    /**
 * Constructs a HospitalSpecialty Object using HospitalID and specialization
 */
    public HospitalSpecialty(int hospitalID, String specialization) {
        setHospitalID(hospitalID);
        setSpecialization(specialization);
    }
    /**
 * Creates the table for HospitalSpecialty
 */	
    public static void createTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(
            "CREATE TABLE HospitalSpecialty ( " +
                "hospitalID int NOT NULL, " +
                "specialization VARCHAR(255) NOT NULL, " +
                "CONSTRAINT fk_hs_hospitalID " +
                    "FOREIGN KEY (hospitalID) REFERENCES Hospital(ID) " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE, " +
                "CONSTRAINT fk_hs_specialization " +
                    "FOREIGN KEY (specialization) REFERENCES Specialization(name) " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE " +
            "); "
            );
    }
    /**
 * drops the Hospital Specialty table
 */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS HospitalSpecialty;");
    }
    /**
 * inserts into HospitalSpecialty
 */
    public void insert() throws SQLException {
        String query = "INSERT INTO HospitalSpecialty VALUES ( ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, getHospitalID());
        statement.setString(2, getSpecialization());

        statement.executeUpdate();
    }
    /**
 * updates the HospitalSpecialty relation
 * cannot be updated
 */
    public void update() {
        throw new UnsupportedOperationException("Cannot update a 2-way relation. Delete and create a new one.");
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
     * Get specialization.
     *
     * @return specialization as String.
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Set specialization.
     *
     * @param specialization the value to set.
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
