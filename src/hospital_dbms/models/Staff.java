package hospital_dbms.models;

import static hospital_dbms.HospitalDBMS.getConnection;
import static hospital_dbms.Reports.printResultsSet;
import hospital_dbms.models.Person;
import java.sql.*;

/**
 * Represents someone who works at the Hospital
 * Inherits Person for basic personal information
 */
public class Staff extends Person {
    // Possible job titles for a Staff member 
    public enum JobTitle {
        NURSE,
        DOCTOR,
        ADMINISTRATOR,
        BILLER
    };

    // Staff member's job
    private JobTitle jobTitle;
    // The hospital the Staff member works at
    int hospitalID;
    // The department the Staff member works in
    private String department;
    // The Staff member's professional title
    private String profTitle;
    // Address of the Staff member's office
    private String officeAddress;
    // Specialization of a Doctor (only applicable if this.jobTitle is DOCTOR)
    private String specialization;

    /** Construct a Staff object from all properties (including personal details) */
    public Staff(String name, String address, String phoneNum, Date dateOfBirth,
                 JobTitle jobTitle, int hospitalID, String department, String profTitle, String officeAddress, String specialization) {
        super(name, address, phoneNum, dateOfBirth);
        setJobTitle(jobTitle);
        setHospitalID(hospitalID);
        setDepartment(department);
        setProfTitle(profTitle);
        setOfficeAddress(officeAddress);
        setSpecialization(specialization);
    }

    /** Construct a Staff object from a Person plus extra attributes */
    public Staff(Person person, JobTitle jobTitle, int hospitalID, String department, String profTitle, String officeAddress, String specialization) {
        this(person.getName(), person.getAddress(), person.getPhoneNum(), person.getDateOfBirth(),
             jobTitle, hospitalID, department, profTitle, officeAddress, specialization);
    }

    /** CREATE the Staff table if it doesn't exist */
    public static void createTable() throws SQLException {
        Connection c = getConnection();
        Person.createTable(); // Create parent table if not exists
        Statement statement = c.createStatement();

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Staff ( " +
                "personID int NOT NULL, " +
                "hospitalID int NOT NULL, " +
                "department VARCHAR(255), " +
                "jobTitle VARCHAR(255), " +
                "profTitle VARCHAR(255), " +
                "officeAddress VARCHAR(255), " +
                "specialization VARCHAR(255), " +
                "CONSTRAINT fk_staff_personID " +
                "  FOREIGN KEY (personID) REFERENCES Person(ID) " +
                "  ON DELETE CASCADE " +
                "  ON UPDATE CASCADE, " +
                "CONSTRAINT fk_staff_hospitalID " +
                "  FOREIGN KEY (hospitalID) REFERENCES Hospital(ID) " +
                "  ON DELETE CASCADE " +
                "  ON UPDATE CASCADE, " +
                "CONSTRAINT fk_staff_specialization " +
                "  FOREIGN KEY (specialization) REFERENCES Specialization(name) " +
                "  ON DELETE SET NULL " +
                "  ON UPDATE CASCADE " +
                "); "
        );
    }

    /** DROP the staff table if it does exist */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS Staff;");
    }

    /** INSERT the current object into the database */
    public void insert() throws SQLException {
        try {
            connection.setAutoCommit(false);
            super.insert();
            String query = "INSERT INTO Staff" +
                        "(personID, hospitalID, department, jobTitle, profTitle, officeAddress, specialization)" +
                        "VALUES ( ?, ?, ?, ?, ?, ?, ? )";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, getID());
            statement.setInt(2, getHospitalID());
            statement.setString(3, getDepartment());
            statement.setString(4, getJobTitle().toString());
            statement.setString(5, getProfTitle());
            statement.setString(6, getOfficeAddress());

            if (getSpecialization() == null) {
                statement.setNull(7, Types.VARCHAR);
            } else {
                statement.setString(7, getSpecialization());
            }

            statement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Error occurred while inserting into Staff.");
            try {
                if(connection!=null) {
                    connection.rollback();
                    System.out.println("Successfully rolled back changes during Staff insert.");
                }
            } catch(SQLException e2) {
                System.out.println("Error occurred while rolling back an insert into Staff: ");
                e2.printStackTrace();
            }

        }
    }

    /** UPDATE the database to reflect the current object */
    public void update() throws SQLException {
        try {
            connection.setAutoCommit(false); // Perform parent and child update as one x-actn
            super.update();

            String query = "UPDATE Staff set hospitalID=?, department=?, jobTitle=?, profTitle=?, officeAddress=?, specialization=? WHERE personID=?";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, getHospitalID());
            statement.setString(2, getDepartment());
            statement.setString(3, getJobTitle().toString());
            statement.setString(4, getProfTitle());
            statement.setString(5, getOfficeAddress());
            if (getSpecialization() == null) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, getSpecialization());
            }
            statement.setInt(7, getID());

            statement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
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

    /** Lookup a Staff by personID and return it as an object */
    public static Staff getByID(int id) throws SQLException {
        Connection connection = getConnection();
        Person parent = Person.getByID(id);
        if(parent == null)
            return null;
        
        String query = "SELECT * FROM Staff WHERE personID = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.absolute(1);
        
        try {
            int storedHospitalID = rs.getInt("hospitalID");
            String storedDepartment = rs.getString("department");
            JobTitle storedJobTitle = JobTitle.valueOf(rs.getString("jobTitle"));
            String storedProfTitle = rs.getString("profTitle");
            String storedOfficeAddress = rs.getString("officeAddress");
            String storedSpecialization = rs.getString("specialization");
            rs.close();
            Staff s = new Staff(parent, storedJobTitle, storedHospitalID, storedDepartment, storedProfTitle, storedOfficeAddress, storedSpecialization);
            s.setID(parent.getID());
            return s;
        } catch(SQLDataException e) {
            System.out.println("Could not find a Staff member with that ID. Please try again.");
            rs.close();
            return null;
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
     * Get department.
     *
     * @return department as String.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Set department.
     *
     * @param department the value to set.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Get profTitle.
     *
     * @return profTitle as String.
     */
    public String getProfTitle() {
        return profTitle;
    }

    /**
     * Set profTitle.
     *
     * @param profTitle the value to set.
     */
    public void setProfTitle(String profTitle) {
        this.profTitle = profTitle;
    }

    /**
     * Get officeAddress.
     *
     * @return officeAddress as String.
     */
    public String getOfficeAddress() {
        return officeAddress;
    }

    /**
     * Set officeAddress.
     *
     * @param officeAddress the value to set.
     */
    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    /**
     * Get jobTitle.
     *
     * @return jobTitle as JobTitle.
     */
    public JobTitle getJobTitle() {
        return jobTitle;
    }

    /**
     * Set jobTitle.
     *
     * @param jobTitle the value to set.
     */
    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
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
        if (specialization != null && this.jobTitle != JobTitle.DOCTOR) {
            throw new IllegalArgumentException("Only doctors can have a specialization");
        }
        this.specialization = specialization;
    }
}
