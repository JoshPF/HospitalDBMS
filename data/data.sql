
INSERT INTO `Billing` VALUES (100,1,'Cash',9),(101,2,'Insurance',10),(103,4,'Credit Card',11),(104,2,'Credit Card',12),(105,1,'Check',9);

INSERT INTO `Doctor` VALUES (5,'Oncology'),(7,'Obstetrics'),(8,'Pediatrics'),(10,'Dental');

INSERT INTO `Hospital` VALUES (1,'401 Oval Drive',2147483647),(2,'206 Cheshire Lane',2147483647),(3,'303 Western Blvd.',2147483647),(4,'100 North Plaza',2147483647);

INSERT INTO `HospitalSpecialty` VALUES (1,'Oncology',40,4),(2,'Orthopedics',15,6),(3,'Pediatrics',50,4),(4,'Dental',10,3),(2,'Dermatology',7,6);

INSERT INTO `HospitalVisit` VALUES (9,2,1,'2019-10-20',NULL,'Oxycontin','Broken Hand'),(10,4,2,'2018-06-01','2018-06-05','Oxycontin','Appendicitis'),(11,2,4,'2015-12-25','2015-12-30','Ibuprofen','Migraine'),(12,1,2,'2017-07-07','2017-07-17',NULL,'Cancer'),(13,3,1,'2019-02-02','2019-02-03','Oxycontin','Broken Hand');

INSERT INTO `MedicalProcedure` VALUES (1,'surgery',10,8,'left ulna set into place after fracture',NULL),(2,'other',11,8,'Administered TDAP vaccine',NULL),(3,'test',12,8,'MRI','Found major stage 4 cancer'),(4,'test',10,8,'X-Ray','Detected radial fracture in left forearm');

INSERT INTO `Patient` VALUES (1,'111223333','Male','in-ward'),(2,'222113333','Female','processing'),(3,'444113333','Male','completing-treatment'),(4,'444115555','Female','completing-treatment');

INSERT INTO `Person` VALUES (1,'test','432 Oval Drive','9198765432','2012-01-01'),(2,'Sandra','433 Oval Drive','9298765432','1996-02-02'),(3,'Lewis','606 Clark Drive','9298765456','1990-12-02'),(4,'Veronica','604 Clark Drive','9398805356','1970-11-09'),(5,'Jack','3315 Wallaby Way','9383914729','1978-08-19'),(6,'Jill','462 Awesome Street','9273924729','1991-06-04'),(7,'Ethan','101 Black Street','2947294729','1989-10-20'),(8,'Lorenzo','303 White Road','2931749572','1998-03-01'),(10,'Charlotte','767 Wintercrest Ave','9389284927','1983-07-02');

INSERT INTO `ResponsibleStaff` VALUES (9,7),(9,7),(10,8),(11,8);

INSERT INTO `Specialization` VALUES ('Anesthesiology'),('Dental'),('Dermatology'),('Obstetrics'),('Oncology'),('Orthopedics'),('Pediatrics');

INSERT INTO `Staff` VALUES (5,1,'Oncology','Head of Surgery','DO','A440'),(6,1,'Oncology','Nurse','NP','A462'),(7,2,'Maternity','Obstetrician','MD','R101'),(8,3,'Pediatrics','Pediatric Neurosurgeon','MD','T1350'),(10,3,'Dental Surgery','Dental Surgeon','DDS','F202');
