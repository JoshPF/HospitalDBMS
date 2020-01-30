-- MySQL dump 10.17  Distrib 10.3.17-MariaDB, for Linux (x86_64)
--
-- Host: classdb2.csc.ncsu.edu    Database: jpschmid
-- ------------------------------------------------------
-- Server version	5.5.60-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Billing`
--

DROP TABLE IF EXISTS `Billing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Billing` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `patientID` int(11) NOT NULL,
  `paymentInfo` varchar(255) NOT NULL,
  `hospitalVisitID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `patientID` (`patientID`),
  KEY `hospitalVisitID` (`hospitalVisitID`),
  CONSTRAINT `Billing_ibfk_1` FOREIGN KEY (`patientID`) REFERENCES `Patient` (`personID`),
  CONSTRAINT `Billing_ibfk_2` FOREIGN KEY (`hospitalVisitID`) REFERENCES `HospitalVisit` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Billing`
--

LOCK TABLES `Billing` WRITE;
/*!40000 ALTER TABLE `Billing` DISABLE KEYS */;
INSERT INTO `Billing` VALUES (100,1,'Cash',9),(101,2,'Insurance',10),(103,4,'Credit Card',11),(104,2,'Credit Card',12),(105,1,'Check',9);
/*!40000 ALTER TABLE `Billing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Doctor`
--

DROP TABLE IF EXISTS `Doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Doctor` (
  `staffID` int(11) NOT NULL,
  `specializationName` varchar(255) NOT NULL,
  KEY `staffID` (`staffID`),
  KEY `specializationName` (`specializationName`),
  CONSTRAINT `Doctor_ibfk_1` FOREIGN KEY (`staffID`) REFERENCES `Staff` (`personID`),
  CONSTRAINT `Doctor_ibfk_2` FOREIGN KEY (`specializationName`) REFERENCES `Specialization` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Doctor`
--

LOCK TABLES `Doctor` WRITE;
/*!40000 ALTER TABLE `Doctor` DISABLE KEYS */;
INSERT INTO `Doctor` VALUES (5,'Oncology'),(7,'Obstetrics'),(8,'Pediatrics'),(10,'Dental');
/*!40000 ALTER TABLE `Doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Hospital`
--

DROP TABLE IF EXISTS `Hospital`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Hospital` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `phoneNum` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Hospital`
--

LOCK TABLES `Hospital` WRITE;
/*!40000 ALTER TABLE `Hospital` DISABLE KEYS */;
INSERT INTO `Hospital` VALUES (1,'401 Oval Drive',2147483647),(2,'206 Cheshire Lane',2147483647),(3,'303 Western Blvd.',2147483647),(4,'100 North Plaza',2147483647);
/*!40000 ALTER TABLE `Hospital` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HospitalSpecialty`
--

DROP TABLE IF EXISTS `HospitalSpecialty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HospitalSpecialty` (
  `hospitalID` int(11) NOT NULL,
  `specializationName` varchar(255) NOT NULL,
  `bedCount` int(11) DEFAULT NULL,
  `charges` int(11) DEFAULT NULL,
  KEY `hospitalID` (`hospitalID`),
  KEY `specializationName` (`specializationName`),
  CONSTRAINT `HospitalSpecialty_ibfk_1` FOREIGN KEY (`hospitalID`) REFERENCES `Hospital` (`ID`),
  CONSTRAINT `HospitalSpecialty_ibfk_2` FOREIGN KEY (`specializationName`) REFERENCES `Specialization` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HospitalSpecialty`
--

LOCK TABLES `HospitalSpecialty` WRITE;
/*!40000 ALTER TABLE `HospitalSpecialty` DISABLE KEYS */;
INSERT INTO `HospitalSpecialty` VALUES (1,'Oncology',40,4),(2,'Orthopedics',15,6),(3,'Pediatrics',50,4),(4,'Dental',10,3),(2,'Dermatology',7,6);
/*!40000 ALTER TABLE `HospitalSpecialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HospitalVisit`
--

DROP TABLE IF EXISTS `HospitalVisit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HospitalVisit` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `hospitalID` int(11) NOT NULL,
  `patientID` int(11) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date DEFAULT NULL,
  `prescriptions` varchar(255) DEFAULT NULL,
  `diagnosis` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `hospitalID` (`hospitalID`),
  KEY `patientID` (`patientID`),
  CONSTRAINT `HospitalVisit_ibfk_1` FOREIGN KEY (`hospitalID`) REFERENCES `Hospital` (`ID`),
  CONSTRAINT `HospitalVisit_ibfk_2` FOREIGN KEY (`patientID`) REFERENCES `Patient` (`personID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HospitalVisit`
--

LOCK TABLES `HospitalVisit` WRITE;
/*!40000 ALTER TABLE `HospitalVisit` DISABLE KEYS */;
INSERT INTO `HospitalVisit` VALUES (9,2,1,'2019-10-20',NULL,'Oxycontin','Broken Hand'),(10,4,2,'2018-06-01','2018-06-05','Oxycontin','Appendicitis'),(11,2,4,'2015-12-25','2015-12-30','Ibuprofen','Migraine'),(12,1,2,'2017-07-07','2017-07-17',NULL,'Cancer'),(13,3,1,'2019-02-02','2019-02-03','Oxycontin','Broken Hand');
/*!40000 ALTER TABLE `HospitalVisit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MedicalProcedure`
--

DROP TABLE IF EXISTS `MedicalProcedure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MedicalProcedure` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('test','surgery','other') DEFAULT NULL,
  `visitID` int(11) NOT NULL,
  `doctorID` int(11) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `testResults` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `visitID` (`visitID`),
  KEY `doctorID` (`doctorID`),
  KEY `proc_type_idx` (`type`),
  CONSTRAINT `MedicalProcedure_ibfk_1` FOREIGN KEY (`visitID`) REFERENCES `HospitalVisit` (`ID`),
  CONSTRAINT `MedicalProcedure_ibfk_2` FOREIGN KEY (`doctorID`) REFERENCES `Doctor` (`staffID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MedicalProcedure`
--

LOCK TABLES `MedicalProcedure` WRITE;
/*!40000 ALTER TABLE `MedicalProcedure` DISABLE KEYS */;
INSERT INTO `MedicalProcedure` VALUES (1,'surgery',10,8,'left ulna set into place after fracture',NULL),(2,'other',11,8,'Administered TDAP vaccine',NULL),(3,'test',12,8,'MRI','Found major stage 4 cancer'),(4,'test',10,8,'X-Ray','Detected radial fracture in left forearm');
/*!40000 ALTER TABLE `MedicalProcedure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Patient`
--

DROP TABLE IF EXISTS `Patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Patient` (
  `personID` int(11) NOT NULL,
  `SSN` varchar(9) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `status` enum('processing','in-ward','completing-treatment') DEFAULT NULL,
  KEY `personID` (`personID`),
  CONSTRAINT `Patient_ibfk_1` FOREIGN KEY (`personID`) REFERENCES `Person` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Patient`
--

LOCK TABLES `Patient` WRITE;
/*!40000 ALTER TABLE `Patient` DISABLE KEYS */;
INSERT INTO `Patient` VALUES (1,'111223333','Male','in-ward'),(2,'222113333','Female','processing'),(3,'444113333','Male','completing-treatment'),(4,'444115555','Female','completing-treatment');
/*!40000 ALTER TABLE `Patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Person`
--

DROP TABLE IF EXISTS `Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Person` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phoneNum` varchar(10) DEFAULT NULL,
  `dateOfBirth` date NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `phone_idx` (`phoneNum`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Person`
--

LOCK TABLES `Person` WRITE;
/*!40000 ALTER TABLE `Person` DISABLE KEYS */;
INSERT INTO `Person` VALUES (1,'test','432 Oval Drive','9198765432','2012-01-01'),(2,'Sandra','433 Oval Drive','9298765432','1996-02-02'),(3,'Lewis','606 Clark Drive','9298765456','1990-12-02'),(4,'Veronica','604 Clark Drive','9398805356','1970-11-09'),(5,'Jack','3315 Wallaby Way','9383914729','1978-08-19'),(6,'Jill','462 Awesome Street','9273924729','1991-06-04'),(7,'Ethan','101 Black Street','2947294729','1989-10-20'),(8,'Lorenzo','303 White Road','2931749572','1998-03-01'),(10,'Charlotte','767 Wintercrest Ave','9389284927','1983-07-02');
/*!40000 ALTER TABLE `Person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ResponsibleStaff`
--

DROP TABLE IF EXISTS `ResponsibleStaff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ResponsibleStaff` (
  `hospitalVisitID` int(11) NOT NULL,
  `staffID` int(11) NOT NULL,
  KEY `hospitalVisitID` (`hospitalVisitID`),
  KEY `staffID` (`staffID`),
  CONSTRAINT `ResponsibleStaff_ibfk_1` FOREIGN KEY (`hospitalVisitID`) REFERENCES `HospitalVisit` (`ID`),
  CONSTRAINT `ResponsibleStaff_ibfk_2` FOREIGN KEY (`staffID`) REFERENCES `Staff` (`personID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ResponsibleStaff`
--

LOCK TABLES `ResponsibleStaff` WRITE;
/*!40000 ALTER TABLE `ResponsibleStaff` DISABLE KEYS */;
INSERT INTO `ResponsibleStaff` VALUES (9,7),(9,7),(10,8),(11,8);
/*!40000 ALTER TABLE `ResponsibleStaff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Specialization`
--

DROP TABLE IF EXISTS `Specialization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Specialization` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Specialization`
--

LOCK TABLES `Specialization` WRITE;
/*!40000 ALTER TABLE `Specialization` DISABLE KEYS */;
INSERT INTO `Specialization` VALUES ('Anesthesiology'),('Dental'),('Dermatology'),('Obstetrics'),('Oncology'),('Orthopedics'),('Pediatrics');
/*!40000 ALTER TABLE `Specialization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Staff`
--

DROP TABLE IF EXISTS `Staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Staff` (
  `personID` int(11) NOT NULL,
  `hospitalID` int(11) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `jobTitle` varchar(255) DEFAULT NULL,
  `profTitle` varchar(255) DEFAULT NULL,
  `officeAddress` varchar(255) DEFAULT NULL,
  KEY `personID` (`personID`),
  KEY `hospitalID` (`hospitalID`),
  CONSTRAINT `Staff_ibfk_1` FOREIGN KEY (`personID`) REFERENCES `Person` (`ID`),
  CONSTRAINT `Staff_ibfk_2` FOREIGN KEY (`hospitalID`) REFERENCES `Hospital` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Staff`
--

LOCK TABLES `Staff` WRITE;
/*!40000 ALTER TABLE `Staff` DISABLE KEYS */;
INSERT INTO `Staff` VALUES (5,1,'Oncology','Head of Surgery','DO','A440'),(6,1,'Oncology','Nurse','NP','A462'),(7,2,'Maternity','Obstetrician','MD','R101'),(8,3,'Pediatrics','Pediatric Neurosurgeon','MD','T1350'),(10,3,'Dental Surgery','Dental Surgeon','DDS','F202');
/*!40000 ALTER TABLE `Staff` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-22 19:40:49
