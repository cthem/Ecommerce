-- MySQL dump 10.13  Distrib 5.7.18, for Linux (x86_64)
--
-- Host: 138.68.100.190    Database: ecommerce_1423_1507
-- ------------------------------------------------------
-- Server version	5.7.18-0ubuntu0.17.04.1

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
-- Table structure for table `conversations`
--

DROP TABLE IF EXISTS `conversations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `residence_id` int(11) NOT NULL,
  `subject` varchar(45) NOT NULL,
  `read_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `read_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sender_id` (`sender_id`,`receiver_id`,`residence_id`),
  KEY `fk_conversations_users1_idx` (`sender_id`),
  KEY `fk_conversations_users2_idx` (`receiver_id`),
  KEY `fk_conversations_residences_idx` (`residence_id`),
  CONSTRAINT `fk_conversations_residences` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_conversations_users1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_conversations_users2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversations`
--

LOCK TABLES `conversations` WRITE;
/*!40000 ALTER TABLE `conversations` DISABLE KEYS */;
INSERT INTO `conversations` VALUES (7,1,2,4,'hi',1,0,0,0);
/*!40000 ALTER TABLE `conversations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `residence_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_residence_id` (`residence_id`),
  CONSTRAINT `fk_residence_id` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL,
  `body` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_from_sender` tinyint(4) NOT NULL DEFAULT '0',
  `deleted_from_receiver` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_messages_conversations1_idx` (`conversation_id`),
  KEY `fk_messages_users_idx` (`user_id`),
  CONSTRAINT `fk_messages_conversations1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_messages_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (17,1,7,'ghghjghj','2017-07-01 13:45:29',0,0),(18,2,7,'aeeeeeeee','2017-07-01 13:45:38',0,0);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `residence_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `guests` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `residence_id_idx` (`residence_id`),
  KEY `tenant_id_idx` (`tenant_id`),
  CONSTRAINT `residence_id` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tenant_id` FOREIGN KEY (`tenant_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (2,3,3,'2017-06-01','2017-06-01',2),(3,4,4,'2017-06-01','2017-06-03',1),(4,3,1,'2017-06-01','2017-06-01',1);
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `residences`
--

DROP TABLE IF EXISTS `residences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `residences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host_id` int(11) NOT NULL,
  `title` text NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `about` varchar(45) DEFAULT NULL,
  `cancellation_policy` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `rules` varchar(255) DEFAULT NULL,
  `amenities` varchar(255) DEFAULT NULL,
  `floor` int(11) DEFAULT NULL,
  `rooms` int(11) DEFAULT '1',
  `baths` int(11) DEFAULT '0',
  `kitchen` tinyint(1) DEFAULT NULL,
  `living_room` tinyint(1) DEFAULT NULL,
  `view` varchar(50) DEFAULT NULL,
  `space_area` double DEFAULT NULL,
  `photos` varchar(45) DEFAULT NULL,
  `guests` int(11) DEFAULT NULL,
  `available_date_start` date DEFAULT NULL,
  `available_date_end` date DEFAULT NULL,
  `min_price` double DEFAULT NULL,
  `additional_cost_per_person` double DEFAULT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `host_id` (`host_id`),
  CONSTRAINT `fk_residences_1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `residences`
--

LOCK TABLES `residences` WRITE;
/*!40000 ALTER TABLE `residences` DISABLE KEYS */;
INSERT INTO `residences` VALUES (1,2,'Great Appartment in Greece!','no','about','no','Greece','fjjgrp','athens','no','no',1,2,2,1,1,'yes',20.5,'no',1,'2017-05-07','2018-05-07',200,50,1),(3,6,'Perfect place for honeymoon!','no','about','no','Netherlands','grifgjr','Amsterdam','no','no',3,1,1,1,1,'no',45.5,'no',1,'2017-05-07','2018-05-07',300,30,1),(4,6,'House next to the beach','no','about','no','Netherlands','grifgjr','Amsterdam','no','no',3,1,1,1,1,'no',45.5,'no',2,'2017-05-07','2018-05-07',300,30,1),(5,6,'Mary\'s perfect place','no','about','no','Netherlands','grifgjr','Amsterdam','no','no',3,1,1,1,1,'no',45.5,'no',1,'2017-05-07','2018-05-07',300,30,1),(6,6,'Unforgattable moments in this room','no','about','no','Netherlands','grifgjr','Amsterdam','no','no',3,1,1,1,1,'no',45.5,'no',1,'2017-05-07','2018-05-07',300,30,1),(8,6,'Test Title Residence','no','about','no','Netherlands','grifgjr','Amsterdam','no','no',3,1,1,1,1,'no',45.5,'no',1,'2017-05-07','2018-05-07',300,30,1),(9,6,'Your Perfect Residence','Apartment','about','no','France','3 Rue de la Cité 75004 ','Paris','no','no',3,1,1,1,1,'no',45.5,'no',1,'2017-05-07','2018-05-07',300,30,1),(10,2,'Residence Type','housr','hk','hdsvnh','greec','rh','fy','fdwf','svj',7,4,2,0,0,'sea',200,'hgt',5,'2017-07-01','2017-07-29',100,50,1),(18,2,'Sea, Wine and love','Apartrment','rhj','gj','d h','ryj','fj','hk','gj',7,8,6,0,0,'t to',78,'vh',58,'2017-06-06','2017-06-30',58,55,1),(20,2,'Sea, Wine and love','Apartrment','rhj','gj','d h','ryj','fj','hk','gj',7,8,6,0,0,'t to',78,'vh',58,'2017-06-06','2017-06-30',58,55,1),(21,2,'Sea, Wine and love','Apartrment','rhj','gj','d h','ryj','fj','hk','gj',7,8,6,0,0,'t to',78,'vh',58,'2017-06-06','2017-06-30',58,55,1),(23,2,'Sea, Wine and love','Apartrment','rhj','gj','d h','ryj','fj','hk','gj',7,8,6,0,0,'t to',78,'vh',58,'2017-06-06','2017-06-30',58,55,1),(24,2,'Residence title','Apartrment','hk','hdsvnh','greec','rh','fy','fdwf','svj',7,4,2,1,1,'sea',200,'wr',5,NULL,NULL,100,50,0),(26,18,'vassotest','Apartrment','hk','hdsvnh','greec','rh','fy','fdwf','svj',7,4,2,0,0,'sea',200,'y r',5,NULL,NULL,100,50,0);
/*!40000 ALTER TABLE `residences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reviews` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `residence_id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `rating` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reviews_residences1_idx` (`residence_id`),
  KEY `fk_reviews_users1_idx` (`host_id`),
  KEY `fk_reviews_users2_idx` (`tenant_id`),
  CONSTRAINT `fk_reviews_residences1` FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviews_users1` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviews_users2` FOREIGN KEY (`tenant_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,1,2,1,'good',4),(3,3,6,4,'very good',6),(4,4,6,2,'good',4),(5,5,6,2,'good',4),(6,6,6,2,'good',4),(8,8,6,2,'good',4),(9,9,6,1,'very good',5);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `searches`
--

DROP TABLE IF EXISTS `searches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `searches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `user_id` (`user_id`,`city`),
  KEY `fk_searches_1_idx` (`user_id`),
  CONSTRAINT `fk_searches_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searches`
--

LOCK TABLES `searches` WRITE;
/*!40000 ALTER TABLE `searches` DISABLE KEYS */;
INSERT INTO `searches` VALUES (69,1,'amsterdam'),(48,1,'athens'),(5,1,'newyork'),(1,1,'Paris'),(2,3,'Berlin'),(4,3,'Paris');
/*!40000 ALTER TABLE `searches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `photo` varchar(45) DEFAULT NULL,
  `registration_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `about` varchar(45) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `host` varchar(45) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `host_id` (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Sissy','Themeli','sissythem','sissythem','sissy@email.com','1236547892','Greece','Athens','img1080896359661212893.jpg','2017-07-02 06:07:07','about','1990-08-05','0'),(2,'Nikiforos','Pittaras','nikpit','nikpit','nik@email.com','1254883587','France','Paris','photooos','2017-06-06 22:36:47','this is demo','2017-06-11','1'),(3,'Eva','Themeli','evathem','evathem','eva@email.com','1258893345','Germany','Berlin',NULL,'2017-05-07 14:43:36',NULL,NULL,'0'),(4,'Minos','Themelis','minosthem','minosthem','minos@email.com','1234559668','Netherlands','Amsterdam',NULL,'2017-05-07 14:43:36',NULL,NULL,'0'),(6,'Anna','Karavokiri','annakara','annakara','anna@email.com','4552587563','Greece','Athens',NULL,'2017-05-07 14:43:36',NULL,NULL,'1'),(18,'vasso',NULL,'vasso','1235','vvv@ddd.com',NULL,'greece','athens','photo','2017-07-01 11:05:26','about',NULL,'0'),(29,'Anatoli','Rontogianni','anatoli','anatoli','anatoli@email.com','6936548796','Greece','Athens','photo path',NULL,'',NULL,NULL),(30,'vasilki','moschou','4124455632','vass@df.com','vasiliki','1q2w3e','','',NULL,NULL,'',NULL,NULL),(31,'georgia','moschou','georgia','1q2w3e','fgy@d.com','46468','','',NULL,NULL,'',NULL,NULL),(57,'yes','bkhoyo','asdf','asdf','hfh@fj.com','46462','','',NULL,'2017-07-02 05:49:42','',NULL,NULL),(59,'eddh','fbhr','sss','sss','svd@tj.com','70484','','',NULL,NULL,'',NULL,NULL),(60,'rgr','rhtu','qqq','qqq','fnfn@b.com','755','','',NULL,NULL,'',NULL,NULL);
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

-- Dump completed on 2017-07-02 21:22:37
