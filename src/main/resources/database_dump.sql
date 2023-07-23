CREATE SCHEMA IF NOT EXISTS `job-listing-scraper` DEFAULT CHARACTER SET utf8;
USE `job-listing-scraper`;
DROP TABLE IF EXISTS `job_listing`;
CREATE TABLE `job_listing` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `posted_date` bigint(20) DEFAULT NULL,
                               `description` text CHARACTER SET utf8mb4,
                               `job_page_url` varchar(255) DEFAULT NULL,
                               `labor_function` varchar(255) DEFAULT NULL,
                               `logo_link` varchar(255) DEFAULT NULL,
                               `organization_title` varchar(255) DEFAULT NULL,
                               `organization_url` varchar(255) DEFAULT NULL,
                               `position_name` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `job_listing_locations`;
CREATE TABLE `job_listing_locations` (
                                         `job_listing_id` bigint(20) NOT NULL,
                                         `location` varchar(255) DEFAULT NULL,
                                         KEY `FKdt2k13tmspvadi2fncudaqan1` (`job_listing_id`),
                                         CONSTRAINT `FKdt2k13tmspvadi2fncudaqan1` FOREIGN KEY (`job_listing_id`) REFERENCES `job_listing` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `job_listing_tags`;
CREATE TABLE `job_listing_tags` (
                                    `job_listing_id` bigint(20) NOT NULL,
                                    `tag` varchar(255) DEFAULT NULL,
                                    KEY `FKt36syd5v7sk65gs3n1yjvrde8` (`job_listing_id`),
                                    CONSTRAINT `FKt36syd5v7sk65gs3n1yjvrde8` FOREIGN KEY (`job_listing_id`) REFERENCES `job_listing` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
