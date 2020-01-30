package hospital_dbms.models;
import static hospital_dbms.HospitalDBMS.getConnection;
import hospital_dbms.models.Relation;
import java.sql.*;

import hospital_dbms.models.Relation;
import static hospital_dbms.HospitalDBMS.getConnection;

/** Billing account for a patient. Each patient can have 0 or more. */
public class Billing extends Relation {
    // Billing(ID, patientID, paymentInfo, billingRecords, hospitalVisitID)
    int ID;
    int patientID;
    String paymentInfo;

    /** Constructor for Billing from properties */
    public Billing(int patientID, String paymentInfo) {
        setPatientID(patientID);
        setPaymentInfo(paymentInfo);
    }

    /**
     * Run a CREATE TABLE to make the Billing table if it doesn't exist
     */
    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();

        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS Billing ( " +
            "ID INT NOT NULL AUTO_INCREMENT, " +
            "patientID int NOT NULL, " +
            "paymentInfo VARCHAR(255), " +
            "PRIMARY KEY (ID), " +
            "CONSTRAINT fk_billing_patientID " +
            "    FOREIGN KEY (patientID) REFERENCES Patient(personID)            " +
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

        statement.executeUpdate("DROP TABLE IF EXISTS Billing;");
    }
    /**
     * Insert the current object into the database
     */
    public void insert() throws SQLException {
        // Connection c = getConnection();
        // c.setAutoCommit(true);
        String query = "INSERT INTO Billing" +
                       "(patientID, paymentInfo)" +
                       "VALUES ( ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, getPatientID());
        if (getPaymentInfo() == null) {
            statement.setNull(2, Types.VARCHAR);
        } else {
            statement.setString(2, getPaymentInfo());
        }

        statement.executeUpdate();

        // Get the id that the database created
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.ID = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating Billing failed, no ID obtained.");
            }
        }
    }
    /**
     * Update the object in the databse to match the Java object
     */
    public void update() throws SQLException {
        String query = "UPDATE Billing set patientID=?, paymentInfo=? WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, getPatientID());
        if (getPaymentInfo() == null) {
            statement.setNull(2, Types.VARCHAR);
        } else {
            statement.setString(2, getPaymentInfo());
        }

        statement.setInt(3, getID());

        statement.executeUpdate();
    }

    public static Billing getById(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM Billing WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        if (!rs.next()) {
            return null;
        }
        rs.absolute(1);

        int storedPatient = rs.getInt("patientID");
        String storedPayment = rs.getString("paymentInfo");
        rs.close();

        Billing b = new Billing(storedPatient, storedPayment);
        b.setID(id);

        return b;
    }

    /**
     * Delete the current object from the database
     */
    public void delete() throws SQLException {
        String query = "DELETE FROM Billing WHERE ID = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, getID());
        statement.executeUpdate();
        connection.commit();
    }

       /**
     * Get patient ID.
     *
     * @return patient ID as int.
     */
    public Integer getPatientID(){
        return patientID;
    }
    /**
     * Set patientID.
     *
     * @param patientID the value to set.
     */
    public void setPatientID(int patientID){
        this.patientID = patientID;
    }
   /**
     * Get payment Info
     *
     * @return payment info as a String
     */
    public String getPaymentInfo(){
        return paymentInfo;
    }
    /**
     * Set payment Info
     *
     * @param paymentInfo the value to set.
     */
    public void setPaymentInfo(String paymentInfo){
        this.paymentInfo = paymentInfo;
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
}
