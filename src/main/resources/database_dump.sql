-- MySQL dump 10.13  Distrib 5.7.42, for Linux (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
--
-- Table structure for table `job_listing`
--

DROP TABLE IF EXISTS `job_listing`;

CREATE TABLE `job_listing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `posted_date` bigint(20) DEFAULT NULL,
  `description` text,
  `job_page_url` varchar(255) DEFAULT NULL,
  `labor_function` varchar(255) DEFAULT NULL,
  `logo_link` varchar(255) DEFAULT NULL,
  `organization_title` varchar(255) DEFAULT NULL,
  `organization_url` varchar(255) DEFAULT NULL,
  `position_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_listing`
--

LOCK TABLES `job_listing` WRITE;
/*!40000 ALTER TABLE `job_listing` DISABLE KEYS */;
/*!40000 ALTER TABLE `job_listing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_listing_locations`
--

DROP TABLE IF EXISTS `job_listing_locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_listing_locations` (
  `job_listing_id` bigint(20) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  KEY `FKdt2k13tmspvadi2fncudaqan1` (`job_listing_id`),
  CONSTRAINT `FKdt2k13tmspvadi2fncudaqan1` FOREIGN KEY (`job_listing_id`) REFERENCES `job_listing` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_listing_locations`
--

LOCK TABLES `job_listing_locations` WRITE;
UNLOCK TABLES;

--
-- Table structure for table `job_listing_tags`
--

DROP TABLE IF EXISTS `job_listing_tags`;
CREATE TABLE `job_listing_tags` (
  `job_listing_id` bigint(20) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  KEY `FKt36syd5v7sk65gs3n1yjvrde8` (`job_listing_id`),
  CONSTRAINT `FKt36syd5v7sk65gs3n1yjvrde8` FOREIGN KEY (`job_listing_id`) REFERENCES `job_listing` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `job_listing_tags`
--

LOCK TABLES `job_listing_tags` WRITE;
UNLOCK TABLES;

-- Dump completed on 2023-07-22 05:33:53
