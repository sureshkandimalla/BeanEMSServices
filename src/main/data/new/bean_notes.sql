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
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notes` (
  `note_id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` date DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `notes_type` varchar(255) DEFAULT NULL,
  `wage_id` bigint DEFAULT NULL,
  `project_id` bigint DEFAULT NULL,
  `immigration_details_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `bank_accountid` bigint DEFAULT NULL,
  `assignment_id` bigint DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`note_id`),
  KEY `FKafmibhee3pvjbaneixv97vl4i` (`project_id`),
  KEY `FKsli9vig4wq94kn8yjshsjn2c` (`employee_id`),
  KEY `FK6r9hmqmnq85x7tb0ohd3poqj9` (`wage_id`),
  KEY `FK650lhfny4wh9qor1d2qb60erm` (`immigration_details_id`),
  KEY `FKpvb5ounuki4f2pkjgisdct092` (`bank_accountid`),
  KEY `FKoa4acmfofxxfdjutymalfqhco` (`assignment_id`),
  KEY `FKod94q2n4gxtsq4prx58j4c08j` (`address_id`)
) ENGINE=MyISAM AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
INSERT INTO `notes` VALUES (6,'2023-08-25','new hire','Genral',NULL,NULL,NULL,1,NULL,NULL,NULL),(5,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,1,NULL,NULL),(4,'2023-08-25','Full time employment notes','Genral',1,NULL,NULL,NULL,NULL,NULL,NULL),(2,'2023-08-25','updated GC','Genral',NULL,NULL,1,NULL,NULL,NULL,NULL),(1,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,1),(3,'2023-08-25','Full time employment notes','Genral',1,NULL,NULL,NULL,NULL,NULL,NULL),(7,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,6),(8,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,6),(9,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,7),(10,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,7),(11,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,8),(12,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,8),(13,'2023-08-25','updated GC','Genral',NULL,NULL,2,NULL,NULL,NULL,NULL),(14,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(15,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(16,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(17,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(18,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(19,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(20,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(21,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(22,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(23,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(24,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(25,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(26,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(27,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(28,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(29,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(30,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(31,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(32,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(33,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(34,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(35,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(36,'2023-08-25','updated GC','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(37,'2023-08-25','new hire','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(38,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(39,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(40,'2023-08-25','updated GC','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(41,'2023-08-25','new hire','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(42,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(43,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(44,'2023-08-25','updated GC','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(45,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(46,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(47,'2023-08-25','updated GC','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(48,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,23),(49,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,23),(50,'2023-08-25','updated GC','Genral',NULL,NULL,7,NULL,NULL,NULL,NULL),(51,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(52,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(53,'2023-08-25','updated GC','Genral',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(54,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,25),(55,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,25),(56,'2023-08-25','updated GC','Genral',NULL,NULL,9,NULL,NULL,NULL,NULL),(57,'2023-08-25','Full time employment notes','Genral',10,NULL,NULL,NULL,NULL,NULL,NULL),(58,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,26),(59,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,26),(60,'2023-08-25','updated GC','Genral',NULL,NULL,10,NULL,NULL,NULL,NULL),(61,'2023-08-25','new hire','Genral',NULL,NULL,NULL,24,NULL,NULL,NULL),(62,'2023-08-25','Full time employment notes','Genral',11,NULL,NULL,NULL,NULL,NULL,NULL),(63,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,27),(64,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,27),(65,'2023-08-25','updated GC','Genral',NULL,NULL,11,NULL,NULL,NULL,NULL),(66,'2023-08-25','new hire','Genral',NULL,NULL,NULL,25,NULL,NULL,NULL),(67,'2023-08-25','Full time employment notes','Genral',12,NULL,NULL,NULL,NULL,NULL,NULL),(68,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,28),(69,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,28),(70,'2023-08-25','updated GC','Genral',NULL,NULL,12,NULL,NULL,NULL,NULL),(71,'2023-08-25','new hire','Genral',NULL,NULL,NULL,26,NULL,NULL,NULL),(72,'2023-08-25','Full time employment notes','Genral',13,NULL,NULL,NULL,NULL,NULL,NULL),(73,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,29),(74,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,29),(75,'2023-08-25','updated GC','Genral',NULL,NULL,13,NULL,NULL,NULL,NULL),(76,'2023-08-25','new hire','Genral',NULL,NULL,NULL,27,NULL,NULL,NULL),(77,'2023-08-25','Full time employment notes','Genral',14,NULL,NULL,NULL,NULL,NULL,NULL),(78,'2023-08-25','Employee Address created','Genral',NULL,NULL,NULL,NULL,NULL,NULL,30),(79,'2023-08-25','Employee Bank account added','Genral',NULL,NULL,NULL,NULL,NULL,NULL,30),(80,'2023-08-25','updated GC','Genral',NULL,NULL,14,NULL,NULL,NULL,NULL),(81,'2023-08-25','new hire','Genral',NULL,NULL,NULL,28,NULL,NULL,NULL),(82,'2023-08-25','Full time employment notes','Genral',15,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
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
