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
-- Table structure for table `employment`
--

DROP TABLE IF EXISTS `employment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employment` (
  `employment_id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` date DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `employment_end_date` varchar(255) DEFAULT NULL,
  `employment_start_date` varchar(255) DEFAULT NULL,
  `lca_number` varchar(255) DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`employment_id`),
  KEY `FKoxwimvjsqtdreyh99fwpq3me7` (`employee_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employment`
--

LOCK TABLES `employment` WRITE;
/*!40000 ALTER TABLE `employment` DISABLE KEYS */;
INSERT INTO `employment` VALUES (1,'2023-08-25','Director','2028-08-21','2023-08-21','sa',1),(2,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',NULL),(3,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',NULL),(4,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',NULL),(5,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',NULL),(6,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',21),(7,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',NULL),(8,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',23),(9,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',24),(10,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',25),(11,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',26),(12,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',27),(13,'2023-08-25','Director','2028-08-21','2023-08-21','LCA1',28);
/*!40000 ALTER TABLE `employment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-22 21:48:37
