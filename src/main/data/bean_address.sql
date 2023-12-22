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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `address_id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` date DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`address_id`),
  KEY `FKq95h5xhq4by6gadnvlkvtbewl` (`employee_id`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',1),(2,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(3,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(4,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',7),(5,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',8),(6,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',9),(7,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',10),(8,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',11),(9,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(10,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(11,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(12,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(13,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(14,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(15,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(16,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(17,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(18,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(19,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(20,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(21,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(22,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(23,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',21),(24,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',NULL),(25,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',23),(26,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',24),(27,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',25),(28,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',26),(29,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',27),(30,'2023-08-25','12477 Emerald Gate Dr','Frsco','USA','TX','75035',28);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-21 22:40:40
