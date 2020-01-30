package hospital_dbms.models;

import java.sql.*;
import java.util.Calendar;

import hospital_dbms.models.Person;
import static hospital_dbms.HospitalDBMS.getConnection;

/**
 * A patient in the system. Inherits Person to achieve basic personal info.
 */
public class Patient extends Person {

    // Possible Statuses the Patient can have
    public enum Status {
        PROCESSING,
        TREATMENT_COMPLETE,
        IN_TREATMENT
    };

    private Integer ssn;
    private String gender;
    public Status status;

    /** Construct a Patient from all properties */
    public Patient(String name, String address, String phoneNum, Date dateOfBirth, Integer ssn, String gender, Status status) {
        super(name, address, phoneNum, dateOfBirth);
        this.ssn = ssn;
        this.gender = gender;
        this.status = status;
    }

    /** Construct a patient from a Person object plus patient-specific fields */
    public Patient(Person person, int ssn, String gender, Status status) {
        this(person.name, person.address, person.phoneNum, person.dateOfBirth, ssn, gender, status);
    }

    /**
     * CREATE the Patient table if it doesn't exist
     * Also creates the Person table if it didn't exist
     */
    public static void createTable() throws SQLException {
        Connection c = getConnection();
        Person.createTable(); // Create parent table if not exists
        Statement statement = c.createStatement();

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Patient (" + 
                "personID int NOT NULL, " +
                "SSN int, " +
                "gender VARCHAR(10), " +
                "status ENUM('PROCESSING', 'TREATMENT_COMPLETE', 'IN_TREATMENT'), " +
                "CONSTRAINT fk_personID " +
                "FOREIGN KEY (personID) REFERENCES Person(ID) " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE" +
                ");"
            );
    }

    /** DROP the patient table if it does exist */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS Patient;");
    }

    /** INSERT the current patient into the database */
    public void insert() throws SQLException {
        try {
            connection.setAutoCommit(false);
            super.insert();
            String query = "INSERT INTO Patient" +
                        "(personID, ssn, gender, status)" +
                        "VALUES ( ?, ?, ?, ? )";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, super.getID());
            if (getSSN() == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, this.ssn);
            }
            statement.setString(3, this.gender);
            statement.setString(4, this.status.toString());

            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error occurred while inserting into Patient.");
            try {
                if(connection!=null) {
                    connection.rollback();
                    System.out.println("Successfully rolled back changes during Patient insert.");
                }
            } catch(SQLException e2) {
                System.out.println("Error occurred while rolling back an insert into Patient: ");
                e2.printStackTrace();
            }

        }
    }

    /** UPDATE the database to match the current object */
    public void update() throws SQLException {
        try {
            connection.setAutoCommit(false); // Perform parent and child update as one x-actn
            super.update();

            String query = "UPDATE Patient set ssn=?, gender=?, status=? WHERE personID=?";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, this.ssn);
            statement.setString(2, this.gender);
            statement.setString(3, this.status.toString());
            statement.setInt(4, this.getID());

            statement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Error occurred while updating an entry in Patient.");
            try {
                if(connection!=null) {
                    connection.rollback();
                    System.out.println("Successfully rolled back changes during Patient update.");
                }
            } catch(SQLException e2) {
                System.out.println("Error occurred while rolling back an update in Patient: ");
                e2.printStackTrace();
            }
        }
    }

    /** Lookup a patient by their personID and return it as an object */
    public static Patient getByID(int id) throws SQLException {
        Connection connection = getConnection();
        Person parent = Person.getByID(id);
        if(parent == null)
            return null;
        String query = "SELECT * FROM Patient WHERE personID = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.absolute(1);

        try {
            int storedID = rs.getInt("personID");
            int storedSSN = rs.getInt("ssn");
            String storedGender = rs.getString("gender");
            Status storedStatus = Status.valueOf((rs.getString("status")));
            rs.close();
            Patient p = new Patient(parent, storedSSN, storedGender, storedStatus);
            p.setID(storedID);
            return p;
        } catch (SQLDataException e) {
            System.out.println("Could not find a Patient with that ID. Please try again.");
            rs.close();
            return null;
        }

        
    }
    /**Gets the current visit of the patient if they have one*/
    public HospitalVisit getCurrentVisit(int hospitalID) throws SQLException {
        String query = "SELECT * FROM HospitalVisit WHERE endDate IS NULL AND patientID=? AND hospitalID=? LIMIT 1";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.setInt(2, hospitalID);

        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            return null;
        }
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
     * Create a HospitalVisit for this patient starting today
     * @param h: The hospital to checkin to
     * @return: A HospitalVisit instance representing the patient's stay
     */
    public HospitalVisit checkin(Hospital h) throws SQLException {
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        HospitalVisit hv = new HospitalVisit(h.getHospitalID(), this.getID(), today, null, null, null, null);
        hv.insert();

        return hv;
    }

    /**
     * Get ssn.
     *
     * @return ssn as int.
     */
    public Integer getSSN() {
        return ssn;
    }

    /**
     * Set ssn.
     *
     * @param ssn the value to set.
     */
    public void setSSN(Integer ssn) {
        this.ssn = ssn;
    }

    /**
     * Get gender.
     *
     * @return gender as String.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set gender.
     *
     * @param gender the value to set.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get status.
     *
     * @return status as Status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set status.
     *
     * @param status the value to set.
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
