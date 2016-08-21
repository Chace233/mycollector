/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : crawl

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2016-08-02 10:21:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for adafruit
-- ----------------------------
DROP TABLE IF EXISTS `adafruit`;
CREATE TABLE `adafruit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `picUrl` varchar(255) DEFAULT NULL,
  `category` varchar(20) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `description` varchar(10000) DEFAULT NULL,
  `technicalDetail` varchar(500) DEFAULT NULL,
  `source` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of adafruit
-- ----------------------------
INSERT INTO `adafruit` VALUES ('13', 'Raspberry Pi 3 Model B Starter Pack - Includes a Raspberry Pi 3', '$99.95', '3058-00.jpg', 'Raspberry Pi', 'Raspberry Pi B+, Pi 2, & Pi 3', null, '', 'https://www.adafruit.com/products/3058');
