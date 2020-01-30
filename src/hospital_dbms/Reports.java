package hospital_dbms;

import java.sql.*;

import hospital_dbms.models.Patient;
import static hospital_dbms.HospitalDBMS.getConnection;

/** 
 * Functions for printing reports about Hospitals and Patients records
 */
public class Reports {

    /**
     * Prints out an SQL table
     * @param tableName Name of the table to print out
     */
    public static void printTable(String tableName) {
        String sanitizedTableName = tableName.replaceAll("[^a-zA-Z]", "");

        String query = "SELECT * FROM " + sanitizedTableName;

        try {
            Connection conn = getConnection();
            Statement stmnt = conn.createStatement();

            ResultSet rs = stmnt.executeQuery(query);
            printResultsSet(rs, 20);
        } catch (SQLException e) {
            System.out.println("Error printing relation");
        }
    }

    /**
     * Displays the Hospitals grouped by their specialties
     */
    public static void printHospitalsBySpecialty() throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();

        String query = "SELECT hospitalID, address, specialization from HospitalSpecialty HS JOIN Hospital H ON HS.hospitalID=H.ID ORDER BY specialization;";

        ResultSet rs = statement.executeQuery(query);

        printResultsSet(rs, 15);
    }

    /**
     * Displays all the Doctors for a Patient
     * @param patientID ID of the Patient to print the doctors of
     */
    public static void printDoctorsForPatient(int patientID) throws SQLException {
        Connection conn = getConnection();
        Patient p = Patient.getByID(patientID);

        String query = (
                "SELECT DISTINCT name FROM " +
                "ResponsibleStaff RS " +
                "  JOIN HospitalVisit HV ON RS.visitID = HV.ID " +
                "  JOIN Staff S ON RS.staffID=S.personID " +
                "  JOIN Person ON S.personID=Person.ID " +
                "WHERE S.jobTitle='DOCTOR' AND HV.patientID=? AND HV.endDate IS NULL"
                );
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, patientID);

        ResultSet rs = statement.executeQuery();

        if (!rs.next()) {
            System.out.println(p.getName() + " is not currently seeing any doctors");
        } else { rs.beforeFirst(); }

        System.out.println(p.getName() + " is currently seeing the following doctors:");

        printResultsSet(rs, 10);

    }

    /**
     * Displays the Overall Hospital Usage in the system
     */
    public static void printHospitalUsage() throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();

        String query = "SELECT HospitalID, COUNT(IF(occupied=FALSE, 1, null)) AS 'Available Beds', COUNT(IF(occupied=TRUE, 1, null)) AS 'Occupied Beds', (COUNT(IF(occupied=TRUE, 1, null)) / COUNT(*)) * 100 AS 'Usage %' FROM Bed GROUP BY HospitalID;";

        ResultSet rs = statement.executeQuery(query);

        printResultsSet(rs, 15);
    }

    /**
     * Prints a summary of the fees billed to a patient for visits starting between the given dates
     */
    public static void printBilling(int patientID, Date startDate, Date endDate) {
        Connection conn = getConnection();
        String query = (
                "SELECT P.name, " +
                "  SUM(registrationFee) AS 'Regitration Fees', " +
                "  SUM(consultaionFee) AS 'Consult Fees', " +
                "  SUM(testFee) AS 'Test Fees', " +
                "  SUM(treatmentFee) AS 'Treatment Fees', " +
                "  SUM(consultaionFee) + SUM(testFee) + SUM(treatmentFee) + SUM(registrationFee) AS 'Total Billed' " +
                "FROM MedicalRecord MR " +
                "  JOIN HospitalVisit HV ON MR.visitID=HV.ID" +
                "  JOIN Person P ON HV.patientID=P.ID " +
                "WHERE startDate < ? AND startDate > ? AND P.ID=?"
                );

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);

            stmnt.setDate(1, endDate);
            stmnt.setDate(2, startDate);
            stmnt.setInt(3, patientID);

            ResultSet rs = stmnt.executeQuery();

            printResultsSet(rs, 16);
        } catch (SQLException e) {
            System.out.println("Error getting billing records for patient. Please try again.");
        }
    }

    /**
     * Prints out the available beds in a Hospital grouped by specialty
     * @param hospitalID The Hospital ID to display Beds for
     */
    public static void printAvailableBeds(int hospitalID) {
        Connection conn = getConnection();
        String query = (
                "SELECT specializationName AS 'Specialization', " +
                "  COUNT(IF(occupied=FALSE, 1, null)) AS 'Available Beds', " +
                "  COUNT(IF(occupied=TRUE, 1, null)) AS 'Occupied Beds' " +
                "FROM Bed WHERE HospitalID=? " +
                "GROUP BY specializationName;"
                );

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);

            stmnt.setInt(1, hospitalID);

            ResultSet rs = stmnt.executeQuery();

            if (!rs.next()) {
                System.out.println("No beds found for hospital with id " + hospitalID);
            } else {
                rs.beforeFirst();
            }

            printResultsSet(rs, 16);
        } catch (SQLException e) {
            System.out.println("Error getting beds. Please try again.");
        }

    }

    /**
     * Displays the number of Patients per month for a given Hospital
     * @param hospitalID the ID of the Hospital to display the number of Patients for
     */
    public static void printPatientsPerMonth(int hospitalID) {
        Connection conn = getConnection();
        String query = (
                "SELECT MONTHNAME(startDate) AS 'Month', " +
                "  COUNT(*) AS 'Visits' " +
                "FROM HospitalVisit " +
                "WHERE hospitalID=? " +
                "GROUP BY MONTHNAME(startDate);"
                );

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);

            stmnt.setInt(1, hospitalID);

            ResultSet rs = stmnt.executeQuery();

            if (!rs.next()) {
                System.out.println("Visits found for hospital with id " + hospitalID);
            } else {
                rs.beforeFirst();
            }

            printResultsSet(rs, 16);
        } catch (SQLException e) {
            System.out.println("Error getting visits. Please try again.");
        }

    }

    /**
     * Prints out a ResultSet from an SQL query
     * @param rs The ResultSet received after running an SQL query
     * @param width The width to make the columns when printing
     */
    public static void printResultsSet(ResultSet rs, int width) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        if (!rs.next()) {
            return;
        } else {
            rs.beforeFirst();
        }
        System.out.print("\n");

        for (int i = 1; i <= columnsNumber; i++) {
            System.out.print(String.format("%" + width + "s | ", rsmd.getColumnName(i)));
        }
        System.out.print("\n");
        for (int i = 0; i < columnsNumber * (width + 3); i++) {
            System.out.print("-");
        }
        System.out.print("\n");

        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                String columnValue = rs.getString(i);
                System.out.print(String.format("%" + width + "s | ", columnValue));
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
}
