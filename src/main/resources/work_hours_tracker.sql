-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: work_hours_tracker
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
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
                           `deadline` datetime(6) DEFAULT NULL,
                           `user_id` bigint DEFAULT NULL,
                           `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
                           `project_name` varchar(255) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
                           `project_description` varchar(255) DEFAULT NULL,
                           `proj_id` bigint NOT NULL AUTO_INCREMENT,
                           `id` bigint DEFAULT NULL,
                           `value` int DEFAULT NULL,
                           PRIMARY KEY (`proj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES ('2024-12-15 10:00:00.000000',101,NULL,'Redesign ','2024-11-29 20:42:32.688300','Redesign der Firmenwebsite mit modernem Design',1,NULL,NULL),('2024-12-20 15:00:00.000000',102,NULL,'Entwicklung ','2024-11-29 20:42:32.688773','Entwicklung einer mobilen App für Android und iOS',2,NULL,NULL),('2024-11-25 09:30:00.000000',103,NULL,'Plattform ','2024-11-30 11:25:24.403291','Entwicklung einer neuen Plattform für Online-Verkäufe',5,NULL,NULL),('2024-12-01 23:59:59.000000',101,NULL,'Erstellen ','2024-11-30 11:25:40.096678','Erstellen und Umsetzen einer Social-Media-Kampagne',19,NULL,NULL),('2024-12-04 00:00:00.000000',NULL,NULL,'create form in vueJs',NULL,'create form in vueJs',123,101,NULL),('2024-12-04 00:00:00.000000',NULL,NULL,'PROJECT create form in vueJs',NULL,'create form in vueJs',124,101,NULL);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
                          `date` datetime(6) DEFAULT NULL,
                          `user_id` bigint DEFAULT NULL,
                          `report_id` bigint NOT NULL AUTO_INCREMENT,
                          `created_at` datetime(6) DEFAULT NULL,
                          `report_source` varchar(255) DEFAULT NULL,
                          `updated_at` datetime(6) DEFAULT NULL,
                          `id` bigint DEFAULT NULL,
                          PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
                        `id` bigint DEFAULT NULL,
                        `created_at` datetime(6) DEFAULT NULL,
                        `task_description` varchar(255) DEFAULT NULL,
                        `task_name` varchar(255) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL,
                        `deadline` datetime(6) DEFAULT NULL,
                        `project_id` bigint DEFAULT NULL,
                        `value` bigint DEFAULT NULL,
                        `task_id` bigint NOT NULL AUTO_INCREMENT,
                        PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,NULL,'Erstellen von Wireframes für die neue Website','Wireframe Design',NULL,'2024-12-15 10:00:00.000000',19,NULL,23),(2,NULL,'Übertragen des bestehenden Inhalts in das neue Design','Content Migration',NULL,'2024-12-15 10:00:00.000000',20,NULL,24),(3,NULL,'Erstellung eines UI-Mockups für die App','UI Mockup',NULL,'2024-12-15 10:00:00.000000',20,NULL,25),(4,NULL,'Implementierung der Backend-API für die mobile App','Backend API',NULL,'2024-12-15 10:00:00.000000',21,NULL,26),(NULL,NULL,'create form in vueJs','TASK create form in vueJs',NULL,'2024-12-05 00:00:00.000000',123,NULL,27),(NULL,NULL,'create form in vueJs','TASK create form in vueJs',NULL,'2024-12-06 00:00:00.000000',123,NULL,28),(NULL,NULL,'create form in vueJs','TASK create form in vueJs',NULL,'2024-12-05 00:00:00.000000',5,NULL,29);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_entry`
--

DROP TABLE IF EXISTS `time_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_entry` (
                              `end_time` datetime(6) DEFAULT NULL,
                              `start_time` datetime(6) DEFAULT NULL,
                              `task_id` bigint DEFAULT NULL,
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `created_at` datetime(6) DEFAULT NULL,
                              `time_period` decimal(21,0) DEFAULT NULL,
                              `updated_at` datetime(6) DEFAULT NULL,
                              `time_entry_id` bigint DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_entry`
--

LOCK TABLES `time_entry` WRITE;
/*!40000 ALTER TABLE `time_entry` DISABLE KEYS */;
INSERT INTO `time_entry` VALUES ('2024-11-01 11:00:00.000000','2024-11-01 09:00:00.000000',23,1,NULL,27510000,NULL,0),('2024-11-02 15:30:00.000000','2024-11-02 14:00:00.000000',25,2,NULL,27100000,NULL,0),('2024-12-01 12:00:00.000000','2024-12-01 10:00:00.000000',25,3,NULL,23435346,NULL,0),('2024-12-15 10:30:00.000000','2024-12-15 09:00:00.000000',25,4,NULL,435345,NULL,0),('2025-01-10 15:00:00.000000','2025-01-10 13:00:00.000000',26,5,NULL,353453453,NULL,0);
/*!40000 ALTER TABLE `time_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `mail_address` varchar(255) DEFAULT NULL,
                        `name` varchar(255) DEFAULT NULL,
                        `created_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-30 16:38:06
