package hospital_dbms.models;
import hospital_dbms.models.Relation;
import java.sql.*;

import static hospital_dbms.HospitalDBMS.getConnection;

/**
 * Stores basic personal info for all individuals in the system (Patients, Staff, and Doctors)
 */
public class Person extends Relation {

    protected int id;
    protected String name;
    protected String address;
    protected String phoneNum;
    protected Date dateOfBirth;

    /** Construct a Person object from all properties */
    public Person(String name, String address, String phoneNum, Date dateOfBirth) {
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.dateOfBirth = dateOfBirth;
    }

    /** CREATE the Person table if it doesn't exist */
    public static void createTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Person (" +
                "ID int NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "address VARCHAR(255) NOT NULL, " +
                "phoneNum VARCHAR(10) NOT NULL, " +
                "dateOfBirth DATE NOT NULL, " +
                "PRIMARY KEY (ID) " +
                ");"
                );
    }

    /** DROP the Person table if it does exist */
    public static void dropTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS Person;");
    }

    /** Insert the current object into the Person table */
    public void insert() throws SQLException {
        String query = "INSERT INTO Person" +
                       "(name, address, phoneNum, dateOfBirth)" +
                       "VALUES ( ?, ?, ?, ? )";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, this.name);
        statement.setString(2, this.address);
        statement.setString(3, this.phoneNum);
        statement.setDate(4, this.dateOfBirth);

        statement.executeUpdate();

        // Get the id that the database created
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating Person failed, no ID obtained.");
            }
        }
    }

    /** UPDATE the database to the current Person object values */
    public void update() throws SQLException {
        String query = "UPDATE Person set name=?, address=?, phoneNum=?, dateOfBirth=? WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, this.name);
        statement.setString(2, this.address);
        statement.setString(3, this.phoneNum);
        statement.setDate(4, this.dateOfBirth);
        statement.setInt(5, this.id);

        statement.executeUpdate();
    }

    /** Lookup a Person in the database by ID and return it as an object */
    public static Person getByID(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM Person WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        rs.absolute(1);

        try {
            int storedID = rs.getInt("ID");
            String storedName = rs.getString("name");
            String storedAddress = rs.getString("address");
            String storedPhoneNum = rs.getString("phoneNum");
            Date storedDate = rs.getDate("dateOfBirth");
            rs.close();

            Person p = new Person(storedName, storedAddress, storedPhoneNum, storedDate);
            p.setID(storedID);
            return p;
        } catch (SQLDataException e) {
            System.out.println("Could not find a Person with that ID. Please try again.");
            rs.close();
            return null;
        }
    }

    /** DELETE the current object form the database */
    public void delete() {
        String query = "DELETE FROM Person WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error occurred while inserting into Staff.");
            try {
                if(connection!=null) {
                    connection.rollback();
                    System.out.println("Successfully rolled back changes during Staff insert.");
                }
            } catch(SQLException e2) {
                System.out.println("Error occurred while rolling back a deletion from Person: ");
                e2.printStackTrace();
            }

        }
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
     * Get phoneNum.
     *
     * @return phoneNum as String.
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Set phoneNum.
     *
     * @param phoneNum the value to set.
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * Get dateOfBirth.
     *
     * @return dateOfBirth as Date.
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set dateOfBirth.
     *
     * @param dateOfBirth the value to set.
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
