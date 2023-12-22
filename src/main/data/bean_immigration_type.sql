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
-- Table structure for table `immigration_type`
--

DROP TABLE IF EXISTS `immigration_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `immigration_type` (
  `immigration_details_id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` date DEFAULT NULL,
  `visa_end_date` date DEFAULT NULL,
  `visa_start_date` date DEFAULT NULL,
  `visa_type` varchar(255) DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`immigration_details_id`),
  KEY `FKbi00p9fer5gobmcrhhaa98u4c` (`employee_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `immigration_type`
--

LOCK TABLES `immigration_type` WRITE;
/*!40000 ALTER TABLE `immigration_type` DISABLE KEYS */;
INSERT INTO `immigration_type` VALUES (1,'2023-08-25','2025-08-21','2023-08-21','GC',1),(2,'2023-08-25','2025-08-21','2023-08-21','GC',11),(3,'2023-08-25','2025-08-21','2023-08-21','GC',NULL),(4,'2023-08-25','2025-08-21','2023-08-21','GC',NULL),(5,'2023-08-25','2025-08-21','2023-08-21','GC',NULL),(6,'2023-08-25','2025-08-21','2023-08-21','GC',NULL),(7,'2023-08-25','2025-08-21','2023-08-21','GC',21),(8,'2023-08-25','2025-08-21','2023-08-21','GC',NULL),(9,'2023-08-25','2025-08-21','2023-08-21','GC',23),(10,'2023-08-25','2025-08-21','2023-08-21','GC',24),(11,'2023-08-25','2025-08-21','2023-08-21','GC',25),(12,'2023-08-25','2025-08-21','2023-08-21','GC',26),(13,'2023-08-25','2025-08-21','2023-08-21','GC',27),(14,'2023-08-25','2025-08-21','2023-08-21','GC',28);
/*!40000 ALTER TABLE `immigration_type` ENABLE KEYS */;
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
