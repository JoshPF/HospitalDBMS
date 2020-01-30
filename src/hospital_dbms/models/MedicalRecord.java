package hospital_dbms.models;

import java.sql.*;

import hospital_dbms.models.Relation;
import static hospital_dbms.HospitalDBMS.getConnection;

/**
 * Represents a Medical Record in the HospitalDBMS system containing the IDs of the HospitalVisit and
 * primary Doctor, as well as Test information, prescriptions, diagnostics, treatment and various fees.
 * Inherets insert and update abstract methods from Relation.
 */
public class MedicalRecord extends Relation {

    private int id;
    private int visitID;
    private int doctorID;
    private String prescription;
    private String diagnostic;
    // Test information
    private String test;
    private String result;
    // Recommended treatment for Patient
    private String treatment;
    // Fees associated with this Medical Record
    private int consultaionFee;
    private int testFee;
    private int treatmentFee;

    /**
     * Constructs a Medical Record
     * @param visitID ID of the HospitalVisit
     * @param doctorID ID of the responsible Doctor
     * @param prescription Prescriptions given to the Patient
     * @param diagnostic Patient's diagnosis
     * @param test The test that was conducted
     * @param result The results of the test conducted
     * @param treatment Recommended treatment
     * @param consultationFee Price of consultation
     * @param testFee Price of the test conducted
     * @param treatmentFee Price of the treatment undergone 
     */
    public MedicalRecord(int visitID, int doctorID, String prescription, String diagnostic, String test, 
                         String result, String treatment, int consultaionFee, int testFee, int treatmentFee) {
        setVisitID(visitID);
        setDoctorID(doctorID);
        setPrescription(prescription);
        setDiagnostic(diagnostic);
        setTest(test);
        setResult(result);
        setTreatment(treatment);
        setConsultaionFee(consultaionFee);
        setTestFee(testFee);
        setTreatmentFee(treatmentFee);
    }

    /**
     * Run a CREATE TABLE to make the Medical Record table if it doesn't exist
     */
    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS MedicalRecord ( " +
                "ID INT NOT NULL AUTO_INCREMENT, " +
                "visitID INT NOT NULL, " +
                "doctorID INT NOT NULL, " +
                "prescription VARCHAR(255), " +
                "diagnostic VARCHAR(255), " +
                "test VARCHAR(255), " +
                "result VARCHAR(255), " +
                "treatment VARCHAR(255), " +
                "consultaionFee INT, " +
                "testFee INT NOT NULL, " +
                "treatmentFee INT, " + 
                "CONSTRAINT fk_mp_visitID " +
                    "FOREIGN KEY (visitID) REFERENCES HospitalVisit(ID) " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE, " +
                "CONSTRAINT fk_doctorID " +
                    "FOREIGN KEY (doctorID) REFERENCES Staff(personID) " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE, "+
                "PRIMARY KEY (ID)" +
                ");");
    }

    /** DROP the MedicalRecord table if it does exist */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS MedicalRecord;");
        connection.commit();
    }

    /** Insert the current object into the MedicalRecord table */
    public void insert() throws SQLException {
        String query = "INSERT INTO MedicalRecord" +
                       "(visitID, doctorID, prescription, diagnostic, test, result, treatment, consultaionFee, testFee, treatmentFee)" +
                       "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, getVisitID());
        statement.setInt(2, getDoctorID());
        statement.setString(3, getPrescription());
        statement.setString(4, getDiagnostic());
        statement.setString(5, getTest());
        statement.setString(6, getResult());
        statement.setString(7, getTreatment());
        statement.setInt(8, getConsultaionFee());
        statement.setInt(9, getTestFee());
        statement.setInt(10, getTreatmentFee());

        statement.executeUpdate();

        // Get the id that the database created
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating MedicalRecord failed, no ID obtained.");
            }
        }
    }

    /**
     * Update the object in the databse to match the Java object
     */
    public void update() throws SQLException {
        String query = "UPDATE MedicalRecord set visitID=?, doctorID=?, prescription=?, diagnostic=?, test=?, result=?, treatment=?, consultaionFee=?, testFee=?, treatmentFee=? WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, getVisitID());
        statement.setInt(2, getDoctorID());
        statement.setString(3, getPrescription());
        statement.setString(4, getDiagnostic());
        statement.setString(5, getTest());
        statement.setString(6, getResult());
        statement.setString(7, getTreatment());
        statement.setInt(8, getConsultaionFee());
        statement.setInt(9, getTestFee());
        statement.setInt(10, getTreatmentFee());

        statement.setInt(11, getID());

        statement.executeUpdate();
    }

    /** Lookup a Person in the database by ID and return it as an object */
    public static MedicalRecord getByID(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM MedicalRecord WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.absolute(1);

        int storedID = rs.getInt("ID");
        int sVisitId = rs.getInt("visitId");
        int sDocId = rs.getInt("doctorID");
        String sPres = rs.getString("prescription");
        String sDiag = rs.getString("diagnostic");
        String sTest = rs.getString("test");
        String sRes = rs.getString("result");
        String sTreat = rs.getString("treatment");
        int sCFee = rs.getInt("consultaionFee");
        int sTestFee = rs.getInt("testFee");
        int sTrFee = rs.getInt("treatmentFee");

        rs.close();

        MedicalRecord mr = new MedicalRecord(sVisitId, sDocId, sPres, sDiag, sTest, sRes, sTreat, sCFee, sTestFee, sTrFee);
        mr.setID(storedID);
        return mr;
    }

    /**
     * Get id.
     *
     * @return id as int.
     */
    public int getID() {
        return id;
    }

    /**
     * Set id.
     *
     * @param id the value to set.
     */
    public void setID(int id) {
        this.id = id;
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
     * Get doctorID.
     *
     * @return doctorID as int.
     */
    public int getDoctorID() {
        return doctorID;
    }

    /**
     * Set doctorID.
     *
     * @param doctorID the value to set.
     */
    public void setDoctorID(int doctorID) {
        // Make sure the person is a doctor
        try {
            Staff allegedDoctor = Staff.getByID(doctorID);
            if (allegedDoctor.getJobTitle() != Staff.JobTitle.DOCTOR) {
                throw new IllegalArgumentException(allegedDoctor.getName() + " is not a doctor");
            }
        } catch (SQLException e) {
            //throw new IllegalArgumentException("Unable to find doctor with id " + doctorID);
        }
        this.doctorID = doctorID;
    }

    /**
     * Get prescription.
     *
     * @return prescription as String.
     */
    public String getPrescription() {
        return prescription;
    }

    /**
     * Set prescription.
     *
     * @param prescription the value to set.
     */
    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    /**
     * Get diagnostic.
     *
     * @return diagnostic as String.
     */
    public String getDiagnostic() {
        return diagnostic;
    }

    /**
     * Set diagnostic.
     *
     * @param diagnostic the value to set.
     */
    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    /**
     * Get test.
     *
     * @return test as String.
     */
    public String getTest() {
        return test;
    }

    /**
     * Set test.
     *
     * @param test the value to set.
     */
    public void setTest(String test) {
        this.test = test;
    }

    /**
     * Get result.
     *
     * @return result as String.
     */
    public String getResult() {
        return result;
    }

    /**
     * Set result.
     *
     * @param result the value to set.
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Get treatment.
     *
     * @return treatment as String.
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Set treatment.
     *
     * @param treatment the value to set.
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     * Get consultaionFee.
     *
     * @return consultaionFee as int.
     */
    public int getConsultaionFee() {
        return consultaionFee;
    }

    /**
     * Set consultaionFee.
     *
     * @param consultaionFee the value to set.
     */
    public void setConsultaionFee(int consultaionFee) {
        this.consultaionFee = consultaionFee;
    }

    /**
     * Get testFee.
     *
     * @return testFee as int.
     */
    public int getTestFee() {
        return testFee;
    }

    /**
     * Set testFee.
     *
     * @param testFee the value to set.
     */
    public void setTestFee(int testFee) {
        this.testFee = testFee;
    }

    /**
     * Get treatmentFee.
     *
     * @return treatmentFee as int.
     */
    public int getTreatmentFee() {
        return treatmentFee;
    }

    /**
     * Set treatmentFee.
     *
     * @param treatmentFee the value to set.
     */
    public void setTreatmentFee(int treatmentFee) {
        this.treatmentFee = treatmentFee;
    }
}
