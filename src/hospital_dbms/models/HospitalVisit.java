package hospital_dbms.models;
import hospital_dbms.models.Relation;
import java.sql.*;

import static hospital_dbms.HospitalDBMS.getConnection;

/**
 * Represents a Patient's checkin/checkout and general info on their visit to a hospital
 */
public class HospitalVisit extends Relation {

    private int ID;
    private int hospitalID;
    private int patientID;
    private Date startDate;
    private Date endDate;
    private String diagnosis;
    private Integer bedNumber;
    private Float registrationFee;

    /**
     * Construct a HospitalVisit from all properties
     */
    public HospitalVisit(int hospitalID, int patientID, Date startDate, Date endDate,
                         String diagnosis, Integer bedNumber, Float registrationFee) {
        setHospitalID(hospitalID);
        setPatientID(patientID);
        setStartDate(startDate);
        setEndDate(endDate);
        setDiagnosis(diagnosis);
        setBedNumber(bedNumber);
        setRegistrationFee(registrationFee);
    }

    /**
     * CREATE the HospitalVisit table if it doesn't exist
     */
    public static void createTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "CREATE TABLE HospitalVisit ( " +
                "ID int NOT NULL AUTO_INCREMENT, " +
                "hospitalID int NOT NULL, " +
                "patientID int NOT NULL, " +
                "startDate DATE NOT NULL, " +
                "endDate DATE, " +
                "diagnosis VARCHAR(255), " +
                "bedNumber INT, " +
                "registrationFee FLOAT, " +
                "PRIMARY KEY (ID), " +
                "CONSTRAINT fk_hospitalID " +
                "    FOREIGN KEY (hospitalID) REFERENCES Hospital(ID) " +
                "    ON DELETE CASCADE " +
                "    ON UPDATE CASCADE, " +
                "CONSTRAINT fk_patientID " +
                "    FOREIGN KEY (patientID) REFERENCES Patient(personID) " +
                "    ON DELETE CASCADE " +
                "    ON UPDATE CASCADE " +
                "); "
                );
    }

    /**
     * DROP the HospitalVisit table if it exists
     */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS HospitalVisit;");
    }

    /**
     * Insert the current object into the database
     */
    public void insert() throws SQLException {
        String query = "INSERT INTO HospitalVisit" +
                       "(hospitalID, patientID, startDate, endDate, diagnosis, bedNumber, registrationFee)" +
                       "VALUES ( ?, ?, ?, ?, ?, ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, getHospitalID());
        statement.setInt(2, getPatientID());
        if (getStartDate() == null) {
            statement.setNull(3, Types.DATE);
        } else {
            statement.setDate(3, getStartDate());
        }
        if (getEndDate() == null) {
            statement.setNull(4, Types.DATE);
        } else {
            statement.setDate(4, getEndDate());
        }
        if (getDiagnosis() == null) {
            statement.setNull(5, Types.VARCHAR);
        } else {
            statement.setString(5, getDiagnosis());
        }
        if (getBedNumber() == null) {
            statement.setNull(6, Types.INTEGER);
        } else {
            statement.setInt(6, getBedNumber());
        }
        if (getRegistrationFee() == null) {
            statement.setNull(7, Types.FLOAT);
        } else {
            statement.setFloat(7, getRegistrationFee());
        }

        statement.executeUpdate();

        // Get the id that the database created
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.ID = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating HospitalVisit failed, no ID obtained.");
            }
        }
    }

    /**
     * Update the database record to match the current object state
     */
    public void update() throws SQLException {
        String query = "UPDATE HospitalVisit set hospitalID=?, patientID=?, startDate=?, endDate=?, diagnosis=?, bedNumber=?, registrationFee=? WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, getHospitalID());
        statement.setInt(2, getPatientID());
        if (getStartDate() == null) {
            statement.setNull(3, Types.DATE);
        } else {
            statement.setDate(3, getStartDate());
        }
        if (getEndDate() == null) {
            statement.setNull(4, Types.DATE);
        } else {
            statement.setDate(4, getEndDate());
        }
        if (getDiagnosis() == null) {
            statement.setNull(5, Types.VARCHAR);
        } else {
            statement.setString(5, getDiagnosis());
        }
        if (getBedNumber() == null) {
            statement.setNull(6, Types.INTEGER);
        } else {
            statement.setInt(6, getBedNumber());
        }
        if (getRegistrationFee() == null) {
            statement.setNull(7, Types.FLOAT);
        } else {
            statement.setFloat(7, getRegistrationFee());
        }

        statement.setInt(8, getID());

        statement.executeUpdate();
    }

    /**
     * Lookup a HospitalVisit by ID and return it as an object
     */
    public static HospitalVisit getByID(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM HospitalVisit WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.absolute(1);

        int storedID = rs.getInt("ID");
        int storedHospitalID = rs.getInt("hospitalID");
        int storedPatientID = rs.getInt("patientID");
        Date storedStartDate = rs.getDate("startDate");
        Date storedEndDate = rs.getDate("endDate");
        String storedDiagnosis = rs.getString("diagnosis");
        Integer storedBedNumber = rs.getInt("bedNumber");
        Float storedRegistrationFee = rs.getFloat("registrationFee");
        rs.close();

        HospitalVisit hv = new HospitalVisit(storedHospitalID, storedPatientID, storedStartDate, storedEndDate, storedDiagnosis, storedBedNumber, storedRegistrationFee);
        hv.setID(storedID);
        return hv;
    }

    /**
     * Delete the current object from the database
     */
    public void delete() {
        String query = "DELETE FROM HospitalVisit WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, getID());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {

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
     * Get patientID.
     *
     * @return patientID as int.
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * Set patientID.
     *
     * @param patientID the value to set.
     */
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    /**
     * Get startDate.
     *
     * @return startDate as Date.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set startDate.
     *
     * @param startDate the value to set.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Get endDate.
     *
     * @return endDate as Date.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Set endDate.
     *
     * @param endDate the value to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Get diagnosis.
     *
     * @return diagnosis as String.
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Set diagnosis.
     *
     * @param diagnosis the value to set.
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Get bedNumber.
     *
     * @return bedNumber as Integer.
     */
    public Integer getBedNumber() {
        return bedNumber;
    }

    /**
     * Set bedNumber.
     *
     * @param bedNumber the value to set.
     */
    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    /**
     * Get registrationFee.
     *
     * @return registrationFee as Float.
     */
    public Float getRegistrationFee() {
        return registrationFee;
    }

    /**
     * Set registrationFee.
     *
     * @param registrationFee the value to set.
     */
    public void setRegistrationFee(Float registrationFee) {
        this.registrationFee = registrationFee;
    }
}
