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
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `project_id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` date DEFAULT NULL,
  `client` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `invoice_term` varchar(255) DEFAULT NULL,
  `payment_term` varchar(255) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `vendor_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  KEY `FK1qckevqd3uypqoembryji9itb` (`employee_id`),
  KEY `FK1mb7kyc06ojaxsros91tekva8` (`vendor_id`),
  KEY `FKj948tru2ilgqxfxsppp9mom5j` (`customer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (2,'2023-08-25','AA','2028-08-21','30','30','SureshAA','2023-08-21','Active',1,NULL,NULL),(3,'2023-08-25','Fedelity','2028-08-21','30','30','Suresh-Fedility','2023-08-21','Active',25,NULL,NULL),(4,'2023-08-25','AA','2028-08-21','30','30','SureshAA','2023-08-21','Active',25,NULL,NULL),(5,'2023-08-25','Fedelity','2028-08-21','30','30','Suresh-Fedility','2023-08-21','Active',26,NULL,NULL),(6,'2023-08-25','AA','2028-08-21','30','30','SureshAA','2023-08-21','Active',26,NULL,NULL),(7,'2023-08-25','Fedelity','2028-08-21','30','30','Suresh-Fedility','2023-08-21','Active',27,NULL,NULL),(8,'2023-08-25','AA','2028-08-21','30','30','SureshAA','2023-08-21','Active',27,NULL,NULL),(9,'2023-08-25','Fedelity','2028-08-21','30','30','Suresh-Fedility','2023-08-21','Active',28,NULL,NULL),(10,'2023-08-25','AA','2028-08-21','30','30','SureshAA','2023-08-21','Active',28,NULL,NULL);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-21 22:40:41
