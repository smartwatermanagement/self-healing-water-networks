-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: swndb
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aggregations`
--

DROP TABLE IF EXISTS `aggregations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aggregations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `issue_count` int(11) DEFAULT '0',
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `aggregations_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `aggregations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aggregations`
--

LOCK TABLES `aggregations` WRITE;
/*!40000 ALTER TABLE `aggregations` DISABLE KEYS */;
INSERT INTO `aggregations` VALUES (1,'IIITB',582,NULL),(2,'MH-1',168,1),(3,'MH-2',0,1),(4,'WH',1,1),(5,'Academic Block',1,1),(6,'Cafeteria',0,1),(7,'Ist Floor',1,2),(8,'2nd Floor',161,2),(9,'3rd Floor',2,2),(10,'4th Floor',4,2);
/*!40000 ALTER TABLE `aggregations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_property_value_map`
--

DROP TABLE IF EXISTS `asset_property_value_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_property_value_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property` varchar(255) NOT NULL,
  `value` text NOT NULL,
  `asset_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `asset_id` (`asset_id`),
  CONSTRAINT `asset_property_value_map_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `assets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_property_value_map`
--

LOCK TABLES `asset_property_value_map` WRITE;
/*!40000 ALTER TABLE `asset_property_value_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset_property_value_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assets`
--

DROP TABLE IF EXISTS `assets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `type` enum('storage','source','outlet','pump','recycling_plant','connection') NOT NULL,
  `issue_count` int(11) DEFAULT '0',
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `aggregation_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `aggregation_id` (`aggregation_id`),
  CONSTRAINT `assets_ibfk_1` FOREIGN KEY (`aggregation_id`) REFERENCES `aggregations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assets`
--

LOCK TABLES `assets` WRITE;
/*!40000 ALTER TABLE `assets` DISABLE KEYS */;
INSERT INTO `assets` VALUES (1,'Main Sump','storage',0,12.844759,77.662399,1),(2,'P1','connection',0,12.844759,77.662399,2),(3,'P2','connection',0,12.844759,77.662399,7),(4,'P6','connection',0,12.844759,77.662399,7),(5,'P7','connection',0,12.844759,77.662399,7),(6,'P3','connection',0,12.844759,77.662399,8),(7,'P8','connection',0,12.844759,77.662399,8),(8,'P9','connection',0,12.844759,77.662399,8),(10,'P4','connection',0,12.844759,77.662399,9),(11,'P10','connection',0,12.844759,77.662399,9),(12,'P11','connection',0,12.844759,77.662399,9),(13,'P5','connection',0,12.844759,77.662399,10),(14,'P12','connection',0,12.844759,77.662399,10),(15,'P13','connection',0,12.844759,77.662399,10),(16,'P14','connection',0,12.844759,77.662399,10),(17,'P15','connection',0,12.844759,77.662399,3),(18,'P16','connection',0,12.844759,77.662399,4),(19,'P17','connection',0,12.844759,77.662399,5),(20,'P18','connection',0,12.844759,77.662399,6),(21,'BWSSB Tank','storage',0,12.844759,77.662399,1);
/*!40000 ALTER TABLE `assets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `connections`
--

DROP TABLE IF EXISTS `connections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `connections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) NOT NULL,
  `to_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `from_id` (`from_id`),
  KEY `to_id` (`to_id`),
  CONSTRAINT `connections_ibfk_1` FOREIGN KEY (`from_id`) REFERENCES `assets` (`id`),
  CONSTRAINT `connections_ibfk_2` FOREIGN KEY (`to_id`) REFERENCES `assets` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `connections`
--

LOCK TABLES `connections` WRITE;
/*!40000 ALTER TABLE `connections` DISABLE KEYS */;
INSERT INTO `connections` VALUES (1,1,2),(2,2,3),(3,2,6),(4,2,10),(5,2,13),(6,3,4),(7,3,5),(8,6,7),(9,6,8),(10,10,11),(11,10,12),(12,13,14),(13,13,15),(14,2,16),(15,1,17),(16,1,18),(17,21,19),(18,21,20);
/*!40000 ALTER TABLE `connections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issues`
--

DROP TABLE IF EXISTS `issues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issues` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_id` int(11) DEFAULT NULL,
  `aggregation_id` int(11) DEFAULT NULL,
  `type` enum('threshold_breach','leak','water_requirement_prediction','water_garden') NOT NULL,
  `status` enum('new','in_progress','resolved') NOT NULL DEFAULT 'new',
  `details` text,
  `created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `asset_id` (`asset_id`),
  KEY `aggregation_id` (`aggregation_id`),
  CONSTRAINT `issues_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `assets` (`id`),
  CONSTRAINT `issues_ibfk_2` FOREIGN KEY (`aggregation_id`) REFERENCES `aggregations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=897 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issues`
--

LOCK TABLES `issues` WRITE;
/*!40000 ALTER TABLE `issues` DISABLE KEYS */;
INSERT INTO `issues` VALUES (1,21,1,'threshold_breach','new',NULL,'2014-11-20 12:53:07','2014-11-20 12:51:26'),(2,19,5,'leak','new',NULL,'2014-11-20 12:53:07','2014-11-20 12:52:38'),(576,16,NULL,'leak','new',NULL,'2014-11-24 16:17:09','2014-11-24 16:17:09'),(586,10,NULL,'leak','new',NULL,'2014-11-24 16:20:27','2014-11-24 16:20:27'),(595,3,NULL,'leak','new',NULL,'2014-11-24 16:21:59','2014-11-24 16:21:59'),(718,18,NULL,'leak','new',NULL,'2014-11-24 16:33:04','2014-11-24 16:33:04'),(785,13,NULL,'leak','new',NULL,'2014-11-24 19:04:48','2014-11-24 19:04:48'),(857,19,NULL,'leak','new',NULL,'2014-11-24 19:10:48','2014-11-24 19:10:48'),(859,10,NULL,'leak','new',NULL,'2014-11-24 19:10:50','2014-11-24 19:10:50'),(873,13,NULL,'leak','new',NULL,'2014-11-24 19:18:47','2014-11-24 19:18:47'),(874,NULL,NULL,'water_requirement_prediction','new','{\'requirement\':\'625085\', \'available\':\'235839\', \'last_day_usage\': \'122925\'}','2014-11-24 19:20:48','2014-11-24 19:20:48'),(875,6,NULL,'leak','new',NULL,'2014-11-24 19:20:48','2014-11-24 19:20:48'),(876,NULL,NULL,'water_requirement_prediction','new','{\'requirement\':\'222647\', \'available\':\'417128\', \'last_day_usage\': \'15551\'}','2014-11-24 19:21:22','2014-11-24 19:21:22'),(877,13,NULL,'leak','new',NULL,'2014-11-24 19:21:22','2014-11-24 19:21:22'),(878,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'69\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:23','2014-11-24 19:21:23'),(879,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'39\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:23','2014-11-24 19:21:23'),(880,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'74\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:24','2014-11-24 19:21:24'),(881,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'97\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:24','2014-11-24 19:21:24'),(882,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'92\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:24','2014-11-24 19:21:24'),(883,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'36\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:24','2014-11-24 19:21:24'),(884,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'70\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:25','2014-11-24 19:21:25'),(885,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'72\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:25','2014-11-24 19:21:25'),(886,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'29\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:25','2014-11-24 19:21:25'),(887,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'45\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:26','2014-11-24 19:21:26'),(888,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'30\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:26','2014-11-24 19:21:26'),(889,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'98\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:26','2014-11-24 19:21:26'),(890,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'93\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:27','2014-11-24 19:21:27'),(891,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'85\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:27','2014-11-24 19:21:27'),(892,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'47\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:27','2014-11-24 19:21:27'),(893,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'70\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:28','2014-11-24 19:21:28'),(894,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'91\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:29','2014-11-24 19:21:29'),(895,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'21\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:29','2014-11-24 19:21:29'),(896,6,NULL,'threshold_breach','new','{\'asset_id\':\'6\',\'current_value\':\'25\',\'value\': \'20\', \'property\':\'flow\', \'operator\':\'gt\'}','2014-11-24 19:21:29','2014-11-24 19:21:29');
/*!40000 ALTER TABLE `issues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `read_status` tinyint(1) DEFAULT '0',
  `user_id` int(11) DEFAULT NULL,
  `issue_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `issue_id` (`issue_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`issue_id`) REFERENCES `issues` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=858 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (818,0,1,874),(819,1,1,875),(820,0,1,876),(821,1,1,877),(822,0,1,878),(823,0,1,878),(824,1,1,879),(825,0,1,879),(826,0,1,880),(827,0,1,880),(828,0,1,881),(829,0,1,881),(830,0,1,882),(831,0,1,882),(832,0,1,883),(833,0,1,883),(834,0,1,884),(835,0,1,884),(836,0,1,885),(837,0,1,885),(838,0,1,886),(839,0,1,886),(840,0,1,887),(841,0,1,887),(842,0,1,888),(843,0,1,888),(844,0,1,889),(845,0,1,889),(846,0,1,890),(847,0,1,890),(848,0,1,891),(849,0,1,891),(850,0,1,892),(851,0,1,892),(852,0,1,893),(853,0,1,893),(854,0,1,894),(855,0,1,894),(856,1,1,895),(857,0,1,895);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensors`
--

DROP TABLE IF EXISTS `sensors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensors` (
  `id` int(11) NOT NULL DEFAULT '0',
  `type` enum('flow','quality','level') NOT NULL,
  `asset_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `asset_id` (`asset_id`),
  CONSTRAINT `sensors_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `assets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensors`
--

LOCK TABLES `sensors` WRITE;
/*!40000 ALTER TABLE `sensors` DISABLE KEYS */;
INSERT INTO `sensors` VALUES (0,'flow',16),(1,'flow',3),(2,'flow',6),(3,'flow',10),(4,'flow',13),(5,'level',1),(6,'quality',1),(7,'flow',1),(8,'flow',17),(9,'flow',18),(10,'flow',19),(11,'flow',20);
/*!40000 ALTER TABLE `sensors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscriptions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issueType` enum('threshold_breach','leak','water_requirement_prediction','water_garden') NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `aggregation_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `aggregation_id` (`aggregation_id`),
  CONSTRAINT `subscriptions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `subscriptions_ibfk_2` FOREIGN KEY (`aggregation_id`) REFERENCES `aggregations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
INSERT INTO `subscriptions` VALUES (1,'threshold_breach',1,1),(2,'water_requirement_prediction',1,NULL),(3,'threshold_breach',1,2),(4,'leak',1,NULL);
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thresholds`
--

DROP TABLE IF EXISTS `thresholds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thresholds` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_id` int(11) NOT NULL,
  `property` varchar(255) NOT NULL,
  `operator` varchar(255) NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `asset_id` (`asset_id`),
  CONSTRAINT `thresholds_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `assets` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thresholds`
--

LOCK TABLES `thresholds` WRITE;
/*!40000 ALTER TABLE `thresholds` DISABLE KEYS */;
INSERT INTO `thresholds` VALUES (1,2,'flow','gt','20'),(2,1,'level','lt','20'),(3,6,'flow','gt','20');
/*!40000 ALTER TABLE `thresholds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Kumudini Kakwani','8904642247','Kumudini','Abhijith');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-25 11:15:45
