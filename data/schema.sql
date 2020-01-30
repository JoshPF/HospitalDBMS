CREATE TABLE Hospital(
    ID int NOT NULL AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    phoneNum VARCHAR(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE Person(
    ID int NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phoneNum VARCHAR(10) NOT NULL,
    dateOfBirth DATE NOT NULL,
    PRIMARY KEY (ID)
);


CREATE TABLE Patient (
    personID int NOT NULL,
    SSN int,
    gender VARCHAR(10),
    status ENUM('processing', 'in-ward', 'completing-treatment'),
    CONSTRAINT fk_personID
        FOREIGN KEY (personID) REFERENCES Person(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Specialization (
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE HospitalVisit (
    ID int NOT NULL AUTO_INCREMENT,
    hospitalID int NOT NULL,
    patientID int NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE,
    prescriptions VARCHAR(255),
    diagnosis VARCHAR(255),
    PRIMARY KEY (ID),
    CONSTRAINT fk_hospitalID
        FOREIGN KEY (hospitalID) REFERENCES Hospital(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_patientID
        FOREIGN KEY (patientID) REFERENCES Patient(personID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Staff (
    personID int NOT NULL,
    hospitalID int NOT NULL,
    department VARCHAR(255),
    jobTitle VARCHAR(255),
    profTitle VARCHAR(255),
    officeAddress VARCHAR(255),
    CONSTRAINT fk_staff_personID
        FOREIGN KEY (personID) REFERENCES Person(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_staff_hospitalID
        FOREIGN KEY (hospitalID) REFERENCES Hospital(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE Billing (
    ID int NOT NULL AUTO_INCREMENT,
    patientID int NOT NULL,
    paymentInfo varchar(255) NOT NULL,
    hospitalVisitID int NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT fk_billing_patientID
        FOREIGN KEY (patientID) REFERENCES Patient(personID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_billing_hospitalVisitID
        FOREIGN KEY (hospitalVisitID) REFERENCES HospitalVisit(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE Doctor (
    staffID int NOT NULL,
    specializationName VARCHAR(255) NOT NULL,
    CONSTRAINT fk_doctor_staffID
        FOREIGN KEY (staffID) REFERENCES Staff(personID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_specializationName    
        FOREIGN KEY (specializationName) REFERENCES Specialization(name)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE HospitalSpecialty (
    hospitalID int NOT NULL,
    specializationName VARCHAR(255) NOT NULL,
    charges INT(11),
    bedCount INT(11),
    CONSTRAINT fk_hs_hospitalID
        FOREIGN KEY (hospitalID) REFERENCES Hospital(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_hs_specializationName
        FOREIGN KEY (specializationName) REFERENCES Specialization(name)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE MedicalProcedure(
    ID int NOT NULL AUTO_INCREMENT,
    type ENUM('test', 'surgery', 'other'),
    visitID int NOT NULL,
    doctorID int NOT NULL,
    notes VARCHAR(255),
    testResults VARCHAR(255),
    CONSTRAINT fk_mp_visitID
        FOREIGN KEY (visitID) REFERENCES HospitalVisit(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_doctorID
        FOREIGN KEY (doctorID) REFERENCES Doctor(staffID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    PRIMARY KEY (ID)
);

CREATE TABLE ResponsibleStaff (
    hospitalVisitID int NOT NULL,
    staffID int NOT NULL,
    CONSTRAINT fk_rs_hospitalVisitID
        FOREIGN KEY (hospitalVisitID) REFERENCES HospitalVisit(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_responsible_staffID
          FOREIGN KEY (staffID) REFERENCES Staff(personID)
          ON DELETE CASCADE
          ON UPDATE CASCADE
);

CREATE TABLE Test(
    procedureID int NOT NULL,
    results VARCHAR(255) NOT NULL
);
