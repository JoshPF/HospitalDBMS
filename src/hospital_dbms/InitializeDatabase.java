package hospital_dbms;

import java.sql.*;
import java.util.Calendar;

import static hospital_dbms.HospitalDBMS.getConnection;
import hospital_dbms.models.*;

/**
 * Create sample data for testing and demo purposes
 */
public class InitializeDatabase {

    /**
     * Main method for the HospitalDBMS program. Drops, recreates and populates (using demo data) 
     * all of the tables in the HospitalDBMS system
     * @param args Arguments given at runtime for the program
     * @throws SQLException upon error while connecting to the Database or executing SQL queries
     */
    public static void main(String[] args) throws SQLException {
        Connection c = getConnection();

        // Drop all tables
        Billing.dropTable();
        Bed.dropTable();
        MedicalRecord.dropTable();
        ResponsibleStaff.dropTable();
        HospitalVisit.dropTable();
        Staff.dropTable();
        Patient.dropTable();
        Person.dropTable();
        HospitalSpecialty.dropTable();
        Hospital.dropTable();
        Specialization.dropTable();

        // Create Hospitals
        Hospital.createTable();
        Hospital h1 = new Hospital("111 St NC, 111", "101");
        h1.insert();
        Hospital h2 = new Hospital("222 St NC, 222", "202");
        h2.insert();

        // Create sample specializations
        Specialization.createTable();

        String[] names = {"neurology", "pediatrics", "cardiology", "oncology"};

        for (int i = 0; i < names.length; i++) {
            Specialization s = new Specialization(names[i], 10f);
            s.insert();
        }

        // Assign specializations to hospitals
        HospitalSpecialty.createTable();
        HospitalSpecialty hs1 = new HospitalSpecialty(h1.getHospitalID(), "pediatrics");
        hs1.insert();
        HospitalSpecialty hs2 = new HospitalSpecialty(h1.getHospitalID(), "neurology");
        hs2.insert();
        HospitalSpecialty hs3 = new HospitalSpecialty(h2.getHospitalID(), "cardiology");
        hs3.insert();
        HospitalSpecialty hs4 = new HospitalSpecialty(h2.getHospitalID(), "oncology");
        hs4.insert();


        // Create Patients
        Patient.createTable();
        Patient patient1 = new Patient("John", "81 ABC St , NC 27", "513", Date.valueOf("1980-02-22"),
                                       null, "M", Patient.Status.TREATMENT_COMPLETE);
        patient1.insert();  // ID 3001 in sample data

        Patient patient2 = new Patient("Jason", "82 ABC St , NC 27", "418", Date.valueOf("1999-04-22"),
                                       null, "M", Patient.Status.IN_TREATMENT);
        patient2.insert(); // ID 3002 in sample data

        Patient patient3 = new Patient("William", "99 XYZ Ave, NC 27", "837", Date.valueOf("1999-07-02"),
                                       null, "M", Patient.Status.PROCESSING);
        patient3.insert(); // ID 3002 in sample data

        // Create Staff
        Staff.createTable();
        Staff operator = new Staff("Simpson", "21 ABC St, NC 27", "919", dateFromAge(35),
                                   Staff.JobTitle.BILLER, h1.getHospitalID(), "Billing",
                                   "Accounts Supervisor", h1.getAddress(), null);
        operator.insert();

        Staff nurse1 = new Staff("David", "22 ABC St, NC 27", "123", dateFromAge(45),
                                 Staff.JobTitle.NURSE, h1.getHospitalID(), "Neurology",
                                 "Senior Nurse", h1.getAddress(), null);
        nurse1.insert();


        Staff nurse2 = new Staff("Ruth", "23 ABC St, NC 27", "456", dateFromAge(35),
                                 Staff.JobTitle.NURSE, h2.getHospitalID(), "pediatrics",
                                 "Assistant Nurse", h2.getAddress(), null);
        nurse2.insert();

        Staff nurse3 = new Staff("Sally", "43 DEF St, NC 27", "7892", dateFromAge(26),
                                 Staff.JobTitle.NURSE, h1.getHospitalID(), "pediatrics",
                                 "Junior Nurse", h1.getAddress(), null);
        nurse3.insert();

        Staff nurse4 = new Staff("James", "47 DEF St, NC 27", "9248", dateFromAge(32),
                                 Staff.JobTitle.NURSE, h2.getHospitalID(), "oncology",
                                 "Junior Nurse", h1.getAddress(), null);
        nurse4.insert();

        // Create Doctor Table
        Staff doctor1 = new Staff("Lucy", "42 ABC St, NC 27", "631", dateFromAge(40), Staff.JobTitle.DOCTOR, h1.getHospitalID(), 
                                  "pediatrics", "Senior Surgeon", h1.getAddress(), "pediatrics");
        doctor1.insert();

        Staff doctor2 = new Staff("Steven", "48 ABC St, NC 27", "632", dateFromAge(65), Staff.JobTitle.DOCTOR, h1.getHospitalID(),
                                  "pediatrics", "Senior Surgeon", h1.getAddress(), "pediatrics");
        doctor2.insert();

        Staff doctor3 = new Staff("Joseph", "51 ABC St, NC 27", "327", dateFromAge(41), Staff.JobTitle.DOCTOR, h2.getHospitalID(),
                                  "cardiology", "cardiologist", h1.getAddress(), "cardiology");
        doctor3.insert();

        // Create Beds
        Bed.createTable();
        Bed bed1 = new Bed(h1.getHospitalID(), "neurology", nurse1.getID(), true);
        bed1.insert();

        Bed bed2 = new Bed(h1.getHospitalID(), "neurology", nurse1.getID(), true);
        bed2.insert();

        // Make a BUNCH of beds (several per department of each hospital)
        String[] h1Depts = {"neurology", "pediatrics"};
        String[] h2Depts = {"cardiology", "oncology"};
        for (int i = 0; i < 20; i++) {
            new Bed(h1.getHospitalID(), "neurology", nurse1.getID(), false).insert();
            new Bed(h1.getHospitalID(), "pediatrics", nurse3.getID(), false).insert();
            new Bed(h2.getHospitalID(), "cardiology", nurse4.getID(), false).insert();
            new Bed(h2.getHospitalID(), "oncology", nurse4.getID(), false).insert();
        }

        // Create HospitalVisit
        HospitalVisit.createTable();
        HospitalVisit checkin1 = new HospitalVisit(h1.getHospitalID(), patient1.getID(), Date.valueOf("2019-08-05"),
                                                   null, "abc", bed1.getID(), 20f); // TODO: bed number 5001
        checkin1.insert();

        HospitalVisit checkin2 = new HospitalVisit(h1.getHospitalID(), patient2.getID(), Date.valueOf("2019-10-15"),
                                                   null, "def", bed2.getID(), 20f); // TODO: bed number 5002
        checkin2.insert();

        HospitalVisit checkin3 = new HospitalVisit(h1.getHospitalID(), patient3.getID(), Date.valueOf("2019-11-15"),
                                                   null, "geh", null, 20f); // TODO: bed number 5002
        checkin3.insert();

        // Create ResponsibleStaff
        ResponsibleStaff.createTable();
        new ResponsibleStaff(checkin1.getID(), doctor1.getID()).insert();
        new ResponsibleStaff(checkin1.getID(), nurse1.getID()).insert();
        new ResponsibleStaff(checkin2.getID(), doctor2.getID()).insert();

        // Create MedicalRecords
        MedicalRecord.createTable();
        MedicalRecord mr1 = new MedicalRecord(checkin1.getID(), doctor1.getID(), "antibiotics", "Testing for TB", "TB Blood test", "positive", "TB Treatment", 50, 75, 200);
        mr1.insert();

        MedicalRecord mr2 = new MedicalRecord(checkin1.getID(), doctor1.getID(), "continue antibiotics", "Testing for TB", "X-ray chest (TB) Advanced", "negative", "Not required", 0, 125, 0);
        mr2.insert();

        // Create Billing table
        Billing.createTable();
    }

    /**
     * In some demo data, age is given instead of date of birth
     * Translate to a date (on January 1st) so we can store a Date
     */
    private static Date dateFromAge(int age) {
        Calendar c = Calendar.getInstance();
        c.set(c.get(c.YEAR) - age, c.JANUARY, 1);

        return new Date(c.getTime().getTime());
    }
}
