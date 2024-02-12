-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: beaninfosys
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` date DEFAULT NULL,
  `customer_company_name` varchar(255) DEFAULT NULL,
  `customer_email` varchar(255) DEFAULT NULL,
  `customer_end_date` date DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone` varchar(255) DEFAULT NULL,
  `customer_start_date` date DEFAULT NULL,
  `customer_status` varchar(255) DEFAULT NULL,
  `ein` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'2020-01-01','Orcasio','abc@beaninfosys.com','9999-12-31','HCL Global','123 456 7890','2020-01-01','Active','123456789','www.abc.com'),(2,'2024-01-25','Santender','','9999-12-31','','','2022-01-01','Active','',''),(3,'2024-01-25','Queen consulting','','9999-12-31','','','2022-01-01','Active','',''),(4,'2024-01-25','HCL Global','','9999-12-31','','','2022-01-01','Active','',''),(5,'2024-01-25','Inspire','','9999-12-31','','','2022-01-01','Active','',''),(6,'2024-01-25','Sanrose','','9999-12-31','','','2022-01-01','Active','',''),(7,'2024-01-25','TechStra','','9999-12-31','','','2022-01-01','Active','',''),(8,'2024-01-25','Bristlecone','','9999-12-31','','','2022-01-01','Active','',''),(9,'2024-01-25','SRK','','9999-12-31','','','2022-01-01','Active','',''),(10,'2024-01-25','Tekyantra','','9999-12-31','','','2022-01-01','Active','',''),(11,'2024-01-25','Apptad','','9999-12-31','','','2022-01-01','Active','',''),(12,'2024-01-25','ATT','','9999-12-31','','','2022-01-01','Active','',''),(13,'2024-01-25','ITOpendoors/Pepsi','','9999-12-31','','','2022-01-01','Active','',''),(14,'2024-01-25','Vendorpass','','9999-12-31','','','2022-01-01','Active','',''),(15,'2024-01-25','People2.0','','9999-12-31','','','2022-01-01','Active','',''),(16,'2024-01-25','Agile Enterprise','','9999-12-31','','','2022-01-01','Active','',''),(17,'2024-01-25','Enterprise Solutions','','9999-12-31','','','2022-01-01','Active','',''),(18,'2024-01-25',' KMM Technologies, Inc.','','9999-12-31','','','2022-01-01','Active','',''),(19,'2024-01-25','ACCOLITE ','','9999-12-31','','','2022-01-01','Active','',''),(20,'2024-01-25','SOAL Technologies, LLC ','','9999-12-31','','','2022-01-01','Active','',''),(21,'2024-01-25','Tan Check Consolidated, Inc. ','','9999-12-31','','','2022-01-01','Active','',''),(22,'2024-01-25','BJ Technologies LLC','','9999-12-31','','','2022-01-01','Active','',''),(23,'2024-01-25','Excel Tek Inc ','','9999-12-31','','','2022-01-01','Active','',''),(24,'2024-01-25','BCBS/Valuesoft','','9999-12-31','','','2022-01-01','Active','',''),(25,'2024-01-25','Suhan Technologies','','9999-12-31','','','2022-01-01','Active','',''),(26,'2024-01-25','Shipium','','9999-12-31','','','2022-01-01','Active','',''),(27,'2024-01-25','iPARTNER CONSULTING INC','','9999-12-31','','','2022-01-01','Active','',''),(28,'2024-01-25','Nous Infosystem / BHHC/Saldo','','9999-12-31','','','2022-01-01','Active','',''),(29,'2024-01-25','Hexawar-Fannie Mae','','9999-12-31','','','2022-01-01','Active','',''),(30,'2024-01-25','iMatch Technical Services','','9999-12-31','','','2022-01-01','Active','',''),(31,'2024-01-25','Mohan',NULL,'9999-12-31',NULL,NULL,'2022-01-01','Active',NULL,NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-12 12:31:36
