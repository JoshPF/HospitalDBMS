package hospital_dbms.ui;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.InputMismatchException;

import hospital_dbms.models.*;
import hospital_dbms.models.Patient.Status;
import hospital_dbms.models.Staff.JobTitle;
import hospital_dbms.Reports;
import static hospital_dbms.HospitalDBMS.getConnection;

/**
 * User Interface for the HospitalDBMS system
 */
public class HospitalUI {

    // Connection with the database
    public Connection c;

    /**
     * Sets up and starts the User Interface and repeatedly prompts the user for commands to enter
     */
    public HospitalUI(Connection c) throws SQLException {
        this.c = c;

        // Scanner for user input
        Scanner in = new Scanner( System.in );

        // Instances of Classes
        Bed bed;
        Billing billing;
        Hospital hospital;
        HospitalSpecialty hospitalSpecialty;
        HospitalVisit hospitalVisit;
        MedicalRecord medicalRecord;
        Patient patient;
        Person person;
        ResponsibleStaff responsibleStaff;
        Specialization specialization;
        Staff staff;

        // Used as a temporary variable to store IDs 
        int tmpID;

        while (true) {
            try {
                System.out.print("Enter a command (type \"help\" to see a list of commands or \"quit\" to exit): ");
                String input = in.nextLine();
                input = input.toLowerCase();
                switch (input) {
                    // Help
                    case "help":
                        printCommands();
                        break;
                    // Exit
                    case "quit":
                        System.out.println("Bye.");
                        System.exit(0);
                    // Add Hospital
                    case "ah":
                        hospital = hospitalParameters(in);
                        try {
                            hospital.insert();
                            System.out.printf("Created hospital with id %d\n", hospital.getHospitalID());
                        } catch (SQLException e) {
                            System.out.println("Error creating hospital");
                            e.printStackTrace();
                        }
                        break;
                    // Add Patient
                    case "ap":
                        person = personParameters(in);
                        patient = patientParameters(in, person);
                        try {
                            patient.insert();
                            System.out.printf("Created patient with id %d\n", patient.getID());
                        } catch (SQLException e) {
                            System.out.println("Error creating patient");
                            e.printStackTrace();
                        }
                        break;
                    // Add Staff
                    case "as":
                        person = personParameters(in);
                        staff = staffParameters(in, person);
                        try {
                            staff.insert();
                            System.out.printf("Created staff with id %d\n", staff.getID());
                        } catch (SQLException e) {
                            System.out.println("Error creating patient");
                            e.printStackTrace();
                        }
                        break;
                    // Edit Hospital
                    case "eh":
                        System.out.print("Enter the ID of the Hospital you would like to Edit: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Hospital hospitalToEdit = Hospital.getByID(tmpID);
                        Hospital updatedHospital = hospitalParameters(in);
                        hospitalToEdit.setAddress(updatedHospital.getAddress());
                        hospitalToEdit.setPhone(updatedHospital.getPhone());
                        try {
                            hospitalToEdit.update();
                        } catch (SQLException e) {
                            System.out.println("Error updating hospital");
                            e.printStackTrace();
                        }
                        break;
                    // Edit Patient
                    case "ep":
                        System.out.print("Enter the ID of the Patient you would like to Edit: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        person = personParameters(in);
                        Patient updatedPatient = patientParameters(in, person);
                        updatedPatient.setID(tmpID);
                        try {
                            updatedPatient.update();
                        } catch (SQLException e) {
                            System.out.println("Error updating patient");
                            e.printStackTrace();
                        }
                        break;
                    // Edit Staff
                    case "es":
                        System.out.print("Enter the ID of the Staff you would like to Edit: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        person = personParameters( in );
                        staff = staffParameters( in, person );
                        staff.setID(tmpID);
                        try {
                            staff.update();
                        } catch (SQLException e) {
                            System.out.println("Error updating staff");
                            e.printStackTrace();
                        }
                        break;
                    // Delete Hospital
                    case "dh":
                        System.out.print("Enter the ID of the Hospital you would like to Delete: ");
                        int hospitalID = in.nextInt();
                        in.nextLine();
                        hospital = Hospital.getByID(hospitalID);
                        hospital.delete();
                        break;
                    // Delete Patient
                    case "dp":
                        System.out.print("Enter the ID of the Patient you would like to Delete: ");
                        int patientID = in.nextInt();
                        in.nextLine();
                        patient = Patient.getByID(patientID);
                        if(patient == null)
                            break;
                        patient.delete();
                        break;
                    // Delete Staff
                    case "ds":
                        System.out.print("Enter the ID of the Staff you would like to Delete: ");
                        int staffID = in.nextInt();
                        in.nextLine();
                        staff = Staff.getByID(staffID);
                        if(staff == null)
                            break;
                        staff.delete();
                        break;
                    // Check Beds In Hospital With Specialty
                    case "cbh":
                        System.out.print("Enter hospital ID: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Reports.printAvailableBeds(tmpID);
                        break;
                    // Reserve Bed
                    case "rsb":
                        reserveBed(in);
                        break;
                    // Release Bed
                    case "rlb":
                        releaseBed(in);
                        break;
                    // Transfer Patient to New Hospital
                    case "tp":
		                transferPatient(in);
                        break;
                    // Create Medical Record
                    case "cm":
                        MedicalRecord m = medRecParams(in);
                        m.insert();
                        break;
                    // Edit Medical Record
                    case "em":
                        System.out.print("Enter the ID of the MedicalRecord you would like to edit: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        MedicalRecord updatedMedRec = medRecParams(in);
                        updatedMedRec.setID(tmpID);
                        try {
                            updatedMedRec.update();
                        } catch (SQLException e) {
                            System.out.println("Error updating record");
                            e.printStackTrace();
                        }
                        break;
                    // Create Billing Account
                    case "cba":
                        Billing b = billingParamenters(in);
                        try {
                            b.insert();
                            System.out.printf("Created billing account with id %d\n", b.getID());
                        } catch (SQLException e) {
                            System.out.println("Error creating billing account");
                            e.printStackTrace();
                        }
                        break;
                    // Update Billing Account
                    case "uba":
                        System.out.print("Enter the ID of the Billing Account you would like to update: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Billing b3 = Billing.getById(tmpID);
                        if(b3 == null) {
                            System.out.printf("Billing account with id %d not found\n", tmpID);
                            break;
                        }
                        try {
                            Billing newb2 = billingParamenters(in);
                            newb2.setID(tmpID);
                            newb2.update();
                        } catch (SQLException e) {
                            System.out.println("Error updating billing account");
                            e.printStackTrace();
                        }
                        System.out.printf("Updated billing account %d\n", tmpID);
                        break;
                    // Delete Billing Account
                    case "dba":
                        System.out.print("Enter the ID of the Billing Account you would like to Delete: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Billing b2 = Billing.getById(tmpID);
                        if(b2 == null) {
                            System.out.printf("Billing account with id %d not found\n", tmpID);
                            break;
                        }
                        try {
                            b2.delete();
                        } catch (SQLException e) {
                            System.out.println("Error deleting billing account");
                            e.printStackTrace();
                        }
                        System.out.printf("Deleted billing account %d\n", tmpID);
                        break;
                    // Report Billing History
                    case "rbh":
                        billingReport(in);
                        break;
                    // Overall Hospital Usage
                    case "hu":
                        Reports.printHospitalUsage();
                        break;
                    // Hospital Patients Per Month
                    case "ppm":
                        System.out.print("Enter hospital id: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Reports.printPatientsPerMonth(tmpID);
                        break;
                    // Check-in Patient
                    case "cip":
                        System.out.print("Enter the patient ID: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Patient p = Patient.getByID(tmpID);
                        System.out.print("Enter the Hospital ID to check in: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Hospital h = Hospital.getByID(tmpID);
                        p.checkin(h);
                        break;
                    // Show all Doctors for Patient
                    case "drs":
                        System.out.print("Enter patient id: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Reports.printDoctorsForPatient(tmpID);
                        break;
                    // View Hospitals Grouped By Specialty
                    case "hsp":
                        Reports.printHospitalsBySpecialty();
                        break;
                    case "cop":
                        System.out.print("Enter the patient ID: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        Patient pat = Patient.getByID(tmpID);
                        System.out.print("Enter the Hospital ID to check out: ");
                        tmpID = in.nextInt();
                        in.nextLine();
                        HospitalVisit patientVisit = pat.getCurrentVisit(tmpID);
                        long millis = System.currentTimeMillis();
                        patientVisit.setEndDate(new java.sql.Date(millis));
                        patientVisit.update();
                        break;
                    default:
                        System.out.println("Invalid Command. Please try again.");
                }
            } catch(SQLException s) {
                System.out.println("Exception occurred while communicating with the Database: ");
                s.printStackTrace();
                continue;
            } catch(Exception e) {
                System.out.println("An error has occurred: ");
                e.printStackTrace();
                continue;
            }
        }
    }

    /** Help Command; prints all commands to console */
    public void printCommands() {
        System.out.println("Add Hospital: \"ah\"");
        System.out.println("Add Patient: \"ap\"");
        System.out.println("Add Staff: \"as\"");
        System.out.println("Edit Hospital: \"eh\"");
        System.out.println("Edit Patient: \"ep\"");
        System.out.println("Edit Staff: \"es\"");
        System.out.println("Delete Hospital: \"dh\"");
        System.out.println("Delete Patient: \"dp\"");
        System.out.println("Delete Staff: \"ds\"");
        System.out.println("Check Beds In Hospital With Specialty: \"cbh\"");
        System.out.println("Reserve Bed: \"rsb\"");
        System.out.println("Release Bed: \"rlb\"");
        System.out.println("Transfer Patient to New Hospital: \"tp\"");
        System.out.println("Create Medical Record: \"cm\"");
        System.out.println("Edit Medical Record: \"em\"");
        System.out.println("Create Billing Account: \"cba\"");
        System.out.println("Update Billing Account: \"uba\"");
        System.out.println("Delete Billing Account: \"dba\"");
        System.out.println("Report Billing History: \"rbh\"");
        System.out.println("Overall Hospital Usage: \"hu\"");
        System.out.println("Hospital Patients Per Month: \"ppm\"");
        System.out.println("Check in Patient: \"cip\"");
        System.out.println("Check out Patient: \"cop\"");
        System.out.println("Show all Doctors for Patient: \"drs\"");
        System.out.println("View Hospitals Grouped By Specialty: \"hsp\"\n");
    }

    /**
     * Receives parameters for a Person based on User input
     * @param in Scanner that handles User Input
     */
    public Person personParameters ( Scanner in ) {
        System.out.print( "Enter a name: " );
        String name = in.nextLine();
        System.out.print( "Enter an address: " );
        String addr = in.nextLine();
        System.out.print( "Enter a phone number: " );
        String phoneNum = in.nextLine();

        Date date = null;
        boolean error = true;
        while ( error ) {
            System.out.print( "Enter a date of birth (yyyy-mm-dd): " );
            String dob = in.nextLine();
            try {
                date = Date.valueOf(dob);
                error = false;
            }
            catch ( Exception e ) {
                System.out.println( "Invalid input. Please use the following format for dates: (yyyy-mm-dd)." );
            }
        }
        return new Person(name, addr, phoneNum, date);
    }

    /**
     * Receives parameters for a Patient based on User input
     * @param in Scanner that handles User Input
     * @param person Person object containing name, address, phoneNum and dateOfBirth
     */
    public Patient patientParameters ( Scanner in, Person person ) {
        System.out.print( "Enter an SSN: " );
        int ssn = in.nextInt();
        in.nextLine();
        System.out.print( "Enter gender: " );
        String gender = in.nextLine();

        boolean error = true;
        Status status = null;
        while ( error ) {
            System.out.print( "Enter Patient Status ('PROCESSING', 'TREATMENT_COMPLETE', 'IN_TREATMENT'): " );
            String statusStr = in.nextLine().toLowerCase();
            switch ( statusStr ) {
                case "processing":
                    status = Status.PROCESSING;
                    error = false;
                    break;
                case "treatment_complete":
                    status = Status.TREATMENT_COMPLETE;
                    error = false;
                    break;
                case "in_treatment":
                    status = Status.IN_TREATMENT;
                    error = false;
                    break;
                default:
                    System.out.println("Invalid Patient Status. Please try again.");
            }
        }
        return new Patient(person, ssn, gender, status);
    }

    /**
     * Receives parameters for Staff based on User input
     * @param in Scanner that handles User Input
     * @param person Person object containing name, address, phoneNum and dateOfBirth
     */
    public Staff staffParameters ( Scanner in, Person person ) {
        System.out.print( "Enter a Hospital ID: " );
        int hospitalId = in.nextInt();
        in.nextLine();
        System.out.print( "Enter a department: " );
        String department = in.nextLine();

        System.out.print( "Enter a professional title: " );
        String profTitle = in.nextLine();

        System.out.print( "Enter an office address: " );
        String officeAddress = in.nextLine();

        System.out.print( "Enter a specialization (only for Doctors, leave blank if not): " );
        String specialization = in.nextLine();
        // If the User left Specialization blank, set it to NULL
        specialization = specialization.trim();

        System.out.println("Specialization: " + specialization);
        if(specialization.equals("") || specialization.equals(" "))
            specialization = null;

        boolean error = true;
        JobTitle jobTitle = null;
        while ( error ) {
            System.out.print( "Enter Staff Job Title ('NURSE', 'DOCTOR', 'ADMINISTRATOR', 'BILLER'): " );
            String statusStr = in.nextLine().toLowerCase();
            switch ( statusStr ) {
                case "nurse":
                    jobTitle = JobTitle.NURSE;
                    error = false;
                    break;
                case "doctor":
                    jobTitle = JobTitle.DOCTOR;
                    error = false;
                    break;
                case "administrator":
                    jobTitle = JobTitle.ADMINISTRATOR;
                    error = false;
                    break;
                case "biller":
                    jobTitle = JobTitle.BILLER;
                    error = false;
                    break;
                default:
                    System.out.println( "Invalid Staff Job Title. Please try again." );
            }
        }
        return new Staff( person, jobTitle, hospitalId, department, profTitle, officeAddress, specialization );
    }

    /**
     * Receives parameters for a Hospital based on User input
     * @param in Scanner that handles User Input
     */
    public Hospital hospitalParameters(Scanner in) {
        System.out.print("Enter an address: ");
        String addr = in.nextLine();
        System.out.print("Enter a phone number: ");
        String phone = in.nextLine();
        return new Hospital(addr, phone);
    }

    /**
     * Receives parameters for a Bed based on User input
     * @param in Scanner that handles User Input
     * @param hospital Hospital the bed is located in
     */
    public Bed bedParameters ( Scanner in, Hospital hospital ) {
        System.out.print( "Enter a specialization:" );
        String spec = in.nextLine();

        System.out.print( "Enter a nurse ID: " );
        int nurse = in.nextInt();
        in.nextLine();

        return new Bed( hospital.getHospitalID(), spec, nurse, false);
    }

    /**
     * Receives parameters for a Medical Record based on User input
     * @param in Scanner that handles User Input
     */
    public MedicalRecord medRecParams(Scanner in) {
        System.out.println("Filling information for a Medical Record. If this record is a Test, which has not yet been run, you may leave the test, result, treatment, consultationFee and treatmentFee blank, and update them later using the Edit Medical Record (\"em\") command. All monetary values should be reported to the nearest whole dollar.");
        System.out.print("Enter a HospitalVisit ID: ");
        int visitID = in.nextInt();
        in.nextLine();
        System.out.print("Enter the ID of the Responsible Doctor: ");
        int doctorID = in.nextInt();
        in.nextLine();
        System.out.print("Enter the Patient's prescriptions: ");
        String prescriptions = in.nextLine();
        System.out.print("Patient diagnosis: ");
        String diagnosis = in.nextLine();
        System.out.print("Enter the test run on the Patient: ");
        String test = in.nextLine();
        System.out.print("Enter the results for the test: ");
        String results = in.nextLine();
        System.out.print("Enter the Patient's treatment: ");
        String treatment = in.nextLine();
        System.out.print("Consultation Fee: ");
        int consultationFee = in.nextInt();
        in.nextLine();
        System.out.print("Test Fee (if no test was run, please enter 0): ");
        int testFee = in.nextInt();
        in.nextLine();
        System.out.print("Treatment Fee: ");
        int treatmentFee = in.nextInt();
        in.nextLine();
        
        return new MedicalRecord(visitID, doctorID, prescriptions, diagnosis, test, results, treatment, consultationFee, testFee, treatmentFee);
    }

    /**
     * Receives parameters for Billing based on User input
     * @param in Scanner that handles User Input
     */
    public Billing billingParamenters(Scanner in) {
        System.out.print("Enter patient ID: ");
        int patientID = in.nextInt();
        in.nextLine();

        System.out.println("Enter payment info (credit card number or 'CASH'): ");
        String payment = in.nextLine();

        return new Billing(patientID, payment);
    }

    /**
     * Creates a Billing Report for a Patient between two user specified dates
     * @param in Scanner that handles User Input
     */
    public void billingReport(Scanner in) {
        try {
            System.out.print("Enter a patient id: ");
            int tmpID = in.nextInt();
            in.nextLine();
            System.out.print("Enter the start date (YYYY-MM-dd): ");
            String dateStr = in.nextLine();
            Date startDate = Date.valueOf(dateStr);
            System.out.print("Enter the end date (YYYY-MM-dd): ");
            dateStr = in.nextLine();
            Date endDate = Date.valueOf(dateStr);
            Reports.printBilling(tmpID, startDate, endDate);
        } catch (java.util.InputMismatchException | IllegalArgumentException e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    /**
     * Unoccupies a user specified Bed in the Database
     * @param in Scanner that handles User Input
     */
    public void releaseBed(Scanner in) {
        try {
            System.out.print("Enter bed number: ");
            int bedID = in.nextInt();
            in.nextLine();

            Bed bed = Bed.getById(bedID);
            if (!bed.isOccupied()) {
                System.out.printf("Bed #%d is already unoccupied.\n", bedID);
                return;
            }
            bed.release();
            System.out.printf("Successfully released bed #%d\n", bedID);
        } catch (SQLException e) {
            System.out.println("Error releasing bed. Please try again.");
            e.printStackTrace();
        }

    }

    /**
     * Occupies a Bed in user specified Hospital (and Specialization) for a given Patient
     * @param in Scanner that handles User Input
     */
    public void reserveBed(Scanner in) {
        try {
            System.out.print("Enter hospital id: ");
            int hospitalID = in.nextInt();
            in.nextLine();

            System.out.print("Enter desired specialization: ");
            String specialization = in.nextLine();

            Bed bed = Bed.getAvailableBed(hospitalID, specialization);
            if (bed == null) {
                System.out.printf("No beds are available in hospital #%d with specialization %s\n", hospitalID, specialization);
                return;
            }

            System.out.printf("Found a bed: #%d (overseen by nurse %d)\n", bed.getID(), bed.getNurseID());
            System.out.print("Enter patient id: ");
            int patientID = in.nextInt();
            in.nextLine();

            Patient p = Patient.getByID(patientID);

            HospitalVisit patientVisit = p.getCurrentVisit(hospitalID);
            if (patientVisit == null) {
                System.out.printf("Patient %d is not currently checked in to hospital %d\n", patientID, hospitalID);
            } else if (patientVisit.getBedNumber() != null && patientVisit.getBedNumber() != 0) {
                System.out.printf("Patient %d is already assigned to bed #%d\n", patientID, patientVisit.getBedNumber());
            } else {
                Connection connection = getConnection();
                connection.setAutoCommit(false);
                try {
                    bed.setOccupied(true);
                    patientVisit.setBedNumber(bed.getID());
                    patientVisit.update();
                    bed.update();
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
                System.out.printf("Assigned patient %d to bed #%d\n", patientID, bed.getID());
            }
        } catch (SQLException | InputMismatchException | NullPointerException e) {
            System.out.println("Error reserving bed. Please try again.");
            e.printStackTrace();
            in.nextLine();
        }
    }

    /**
     * Transfers a Patient from one hospital to another. Releases the Patient's old bed and reserves
     * a new bed in the new Hospital with the user specified specialization
     */
 	public void transferPatient(Scanner in) {
        try {
		    System.out.print("Enter patient id: ");
		    int patientID = in.nextInt();
		    in.nextLine();
	        Patient p = Patient.getByID(patientID);
		    //Enter the current Hospital ID    
            System.out.print("Enter current Hospital ID: ");
            int tmpID = in.nextInt();
            in.nextLine();
            HospitalVisit patientVisit = p.getCurrentVisit(tmpID);
            //end the Patient's current hospital Visit
            long millis = System.currentTimeMillis();
            patientVisit.setEndDate(new java.sql.Date(millis));
            patientVisit.update();
		    //release the patient from their bed
            try {
            	Bed bed = Bed.getById(patientVisit.getBedNumber());
             	if (!bed.isOccupied()) {
                	System.out.printf("Bed #%d is already unoccupied.\n", bed.getID());
                	return;
            	}
            	bed.release();
            	System.out.printf("Successfully released bed #%d\n", bed.getID());
        	} catch (SQLException e) {
            	System.out.println("Error releasing bed. Please try again.");
        	}

            //reserve a bed in a new hospital                                                                                                       
            System.out.print("Enter hospital id to Transfer: ");
            int hospitalID = in.nextInt();
            in.nextLine();
			Hospital newHospital = Hospital.getByID(hospitalID);

			//after confirming that there is enough room for the patient, then checkin the patient
 	       	HospitalVisit hv = p.checkin(newHospital);
        } catch (SQLException | InputMismatchException | NullPointerException e) {
            System.out.println("Error reserving bed. Please try again.");
            e.printStackTrace();
            in.nextLine();
        }
    }
}
