-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: bean
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `employee_id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` date DEFAULT NULL,
  `dob` varchar(255) DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `referred_by` varchar(255) DEFAULT NULL,
  `ssn` varchar(255) DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `employment_type` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'2023-08-25','1990-08-21','6098519@gmail.com','Sures','Reddy','6098519867','Suresh','1234567890','Software Devloper','W2','9999-12-31','male','2021-01-01'),(2,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890','Software Devloper','1099','9999-12-31','male','2021-01-01'),(3,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890','Software Devloper','C2C','9999-12-31','male','2021-01-01'),(4,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867',NULL,'1234567890','Software Devloper','C2C','9999-12-31','male','2021-01-01'),(5,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867',NULL,'1234567890','Software Devloper','W2','9999-12-31','male','2021-01-01'),(6,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867',NULL,'1234567890','Software Devloper','1099','9999-12-31','male','2021-01-01'),(7,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867',NULL,'1234567890',NULL,'1099',NULL,NULL,NULL),(8,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867',NULL,'1234567890','Software Devloper','W2','9999-12-31','male','2021-01-01'),(9,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867',NULL,'1234567890','Software Devloper','Full time','9999-12-31','male','2021-01-01'),(10,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867',NULL,'1234567890',NULL,NULL,NULL,NULL,NULL),(11,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(12,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(13,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(14,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(15,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(16,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(17,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(18,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(19,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(20,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(21,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(22,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(23,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(24,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(25,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(26,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(27,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL),(28,'2023-08-25','1990-08-21','6098519@gmail.com','Suresh','Reddy','6098519867','Suresh','1234567890',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-22 21:48:36
