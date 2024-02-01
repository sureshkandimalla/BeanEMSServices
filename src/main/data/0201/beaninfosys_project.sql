-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: beaninfosys
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
  `vendor_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  KEY `FK1mb7kyc06ojaxsros91tekva8` (`vendor_id`),
  KEY `FK1qckevqd3uypqoembryji9itb` (`employee_id`)
) ENGINE=MyISAM AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'9999-12-31','','2023-02-28','30','30','Anji Sajjala','2022-11-01','Inactive',1,2),(2,'9999-12-31','','9999-12-31','30','30','Ashritha Reddy Samanu','2023-01-10','active',2,3),(3,'9999-12-31','','2023-07-14','30','30','Chaitanya Puppala','2022-12-05','Inactive',3,4),(4,'9999-12-31','','9999-12-31','30','30','Chenchu Reddy Allathuru ','2021-10-26','active',4,5),(5,'9999-12-31','','2023-07-31','30','30','Jagannathan  Manogar','2022-12-09','Inactive',5,7),(6,'9999-12-31','','2023-06-30','30','30','Jeevan Reddy Chinthala','2022-12-19','Inactive',6,8),(7,'9999-12-31','','2023-05-10','30','30','Kavya Sri  Koti Reddy','2022-08-29','Inactive',7,9),(8,'9999-12-31','','2023-06-30','30','30','Kuntal Kishore ','2022-10-01','Inactive',5,10),(9,'9999-12-31','','2023-03-31','30','30','Murali krishna  Bisa','2022-11-06','Inactive',8,11),(10,'9999-12-31','','2023-03-31','30','30','Nikhil Nallala','2021-06-01','Inactive',9,13),(11,'9999-12-31','','2023-02-08','30','30','Praveen  Sadineni','2022-01-01','Inactive',10,14),(12,'9999-12-31','','2023-03-10','30','30','Sabitha Muppuri','2023-02-02','Inactive',1,15),(13,'9999-12-31','','9999-12-31','30','30','Saikrishna  Katepalli','2022-01-01','active',11,16),(14,'9999-12-31','','2023-04-10','30','30','Sandeep  Mokirala','2022-12-12','Inactive',5,17),(15,'9999-12-31','','2023-03-31','30','30','Santhosh Chakka','2022-01-01','Inactive',12,18),(16,'9999-12-31','','2023-09-15','30','30','Sindhuja Madishetty','2023-01-23','Inactive',13,19),(17,'9999-12-31','','2023-03-31','30','30','Steven Leiphart ','2022-01-01','Inactive',14,20),(18,'9999-12-31','','2023-12-31','30','30','Venkat Anusuri','2022-01-01','active',15,22),(19,'9999-12-31','','2023-11-30','30','30','Venkat  Reddi','2022-01-01','active',16,23),(20,'9999-12-31','','2023-12-31','30','30','Venkateswara Sai Virat  Puvvala','2022-12-27','active',4,24),(21,'9999-12-31','','2023-06-30','30','30','Vinod Chakoti','2023-01-05','Inactive',5,26),(22,'9999-12-31','','2023-02-28','30','30','Viswanath Meenavalli','2022-03-01','Inactive',2,27),(23,'9999-12-31','','2023-12-31','30','30','Mohan  Yempalla','2022-01-01','active',31,28),(24,'9999-12-31','','2023-02-22','30','30','Vijay Reddy','2022-01-01','Inactive',9,25),(25,'9999-12-31','','9999-12-31','30','30','Kalyan  Chappidi','2023-02-21','active',9,29),(26,'9999-12-31','','9999-12-31','30','30','Ravi Chandra','2023-04-10','active',17,30),(27,'9999-12-31','','2023-06-30','30','30','Vivekananda Mittapelli','2023-03-15','Inactive',15,31),(28,'9999-12-31','','9999-12-31','30','30','Srinivas Reddy Beeram','2023-04-24','active',18,32),(29,'9999-12-31','','9999-12-31','30','30','Sivakumar  Arza','2023-05-19','active',14,33),(30,'9999-12-31','','9999-12-31','30','30','Harshith  Krishnamurthy','2023-05-19','active',14,34),(31,'9999-12-31','','9999-12-31','30','30','Ram krishna raju Naidana','2023-05-03','active',19,35),(32,'9999-12-31','','9999-12-31','30','30','Dikshith Apala','2023-05-31','active',20,6),(33,'9999-12-31','','9999-12-31','30','30','Sunil Kumar','2023-05-27','active',21,21),(34,'9999-12-31','','9999-12-31','30','30','Santhosh Chakka','2023-06-12','active',1,18),(35,'9999-12-31','','9999-12-31','30','30','Sruijan Kumar Thaduri','2023-06-19','active',4,36),(36,'9999-12-31','','9999-12-31','30','30','Jeevan Reddy Chinthala','2023-07-01','active',6,8),(37,'9999-12-31','','9999-12-31','30','30','Sai Charan ANGALAKURTHI','2023-06-12','active',22,37),(38,'9999-12-31','','9999-12-31','30','30','Vijay Kumar Gubbala','2023-08-05','active',23,38),(39,'9999-12-31','','9999-12-31','30','30','Kousalya  Pothuraju','2023-07-10','active',4,39),(40,'9999-12-31','','2023-10-01','30','30','Amarpal Budhiraja','2023-06-12','active',24,40),(41,'9999-12-31','','9999-12-31','30','30','Naga Bhushanam Chappidi','2023-07-10','active',24,41),(42,'9999-12-31','','9999-12-31','30','30','Kesava Kaushik  Kalavagunta','2023-08-28','active',25,42),(43,'9999-12-31','','2024-08-27','30','30','Sabitha Muppuri','2023-08-28','active',26,15),(44,'9999-12-31','','9999-12-31','30','30','Pathanjali Vadlamudi','2023-08-28','active',26,44),(45,'9999-12-31','','9999-12-31','30','30','SreeCharan Reddy Madireddy','2023-09-20','active',27,49),(46,'9999-12-31','','9999-12-31','30','30','Sudheer gopishetty','2023-09-05','active',28,46),(47,'9999-12-31','','9999-12-31','30','30','Shiva Kalyan  Giri','2023-09-05','active',4,47),(48,'9999-12-31','','9999-12-31','30','30','Nikhil Nallala','2023-04-01','active',29,13),(49,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(50,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(51,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(52,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(53,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(54,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(55,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(56,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(57,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(58,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(59,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(60,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(61,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(62,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(63,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48),(64,'9999-12-31','','2023-12-31','30','30','Sneha  Xavier','2023-08-28','active',30,48);
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

-- Dump completed on 2024-02-01  9:45:58
