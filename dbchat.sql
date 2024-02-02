/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.5.57 : Database - dbchat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dbchat` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

USE `dbchat`;

/*Table structure for table `db_article` */

DROP TABLE IF EXISTS `db_article`;

CREATE TABLE `db_article` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `authority` int(11) DEFAULT NULL,
  `publish_time` bigint(20) DEFAULT NULL,
  `content` varchar(650) DEFAULT NULL,
  `img_list` text,
  `view_num` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_article` */

insert  into `db_article`(`id`,`user_id`,`authority`,`publish_time`,`content`,`img_list`,`view_num`,`create_time`,`update_time`,`is_deleted`) values 
(56,289,0,1690530944103,'hhhhsdbnn世界','',0,'2023-07-28 15:55:44','2023-07-28 15:55:44','\0'),
(57,289,0,1690530986177,'天气很好','http://localhost:9999/resource/image/8db17232-c43e-4d3d-9f97-a2f54549fd56.png',0,'2023-07-28 15:56:26','2023-07-28 15:56:26','\0');

/*Table structure for table `db_article_like` */

DROP TABLE IF EXISTS `db_article_like`;

CREATE TABLE `db_article_like` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `article_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_article_like` */

/*Table structure for table `db_authority` */

DROP TABLE IF EXISTS `db_authority`;

CREATE TABLE `db_authority` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `information` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_authority` */

/*Table structure for table `db_comment` */

DROP TABLE IF EXISTS `db_comment`;

CREATE TABLE `db_comment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `article_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `publish_time` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_comment` */

/*Table structure for table `db_comment_like` */

DROP TABLE IF EXISTS `db_comment_like`;

CREATE TABLE `db_comment_like` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `comment_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_comment_like` */

/*Table structure for table `db_friend` */

DROP TABLE IF EXISTS `db_friend`;

CREATE TABLE `db_friend` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `my_id` bigint(20) DEFAULT NULL,
  `friend_id` bigint(20) DEFAULT NULL,
  `notation` varchar(30) DEFAULT NULL COMMENT '备注',
  `session_id` bigint(20) DEFAULT NULL,
  `last_message` varchar(255) DEFAULT NULL,
  `unread` int(11) DEFAULT NULL,
  `is_hidden` bit(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_friend` */

insert  into `db_friend`(`id`,`my_id`,`friend_id`,`notation`,`session_id`,`last_message`,`unread`,`is_hidden`,`create_time`,`update_time`,`is_deleted`) values 
(86,290,289,'Jie',166684400011055104,'【图片】',34,'\0','2023-07-28 15:50:22','2023-08-22 16:05:49','\0'),
(87,289,290,'dd',166684400011055104,'【图片】',31,'\0','2023-07-28 15:50:22','2023-08-22 16:05:49','\0'),
(88,291,290,'dd',166689211242647552,'触发了hand',3,'\0','2023-07-28 16:09:29','2023-08-05 14:58:24','\0'),
(89,290,291,'玛雅',166689211242647552,'触发了hand',3,'\0','2023-07-28 16:09:29','2023-08-05 14:58:24','\0'),
(90,289,295,'账号11',3333333333,'iiiiiiiiiiiiiiiiiii',4,'\0','2023-07-28 16:09:29','2023-08-21 10:50:50','\0'),
(91,295,289,'Jie',3333333333,'iiiiiiiiiiiiiiiiiii',13,'\0','2023-07-28 16:09:29','2023-08-21 10:50:50','\0');

/*Table structure for table `db_group` */

DROP TABLE IF EXISTS `db_group`;

CREATE TABLE `db_group` (
  `id` bigint(20) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_group` */

insert  into `db_group`(`id`,`name`,`avatar`,`introduction`,`owner_id`,`create_time`,`update_time`,`is_deleted`) values 
(166686178702135296,'掘金2221423','http://localhost:9999/resource/image/f6b26402-ae92-43ce-9891-9563fc8a51a6.png','喜欢旅行',289,'2023-07-28 15:57:26','2023-07-28 15:57:26','\0'),
(170297074338893824,'米奇的俱乐部','http://localhost:9999/resource/image/15ccc8ae-7290-4661-a39f-9e176106e928.png','我爱大米',297,'2023-08-07 15:05:51','2023-08-07 15:05:51','\0'),
(170677367608578048,'星群1','http://localhost:9999/resource/image/f6b26402-ae92-43ce-9891-9563fc8a51a6.png','星群1introduction',295,'2023-08-08 16:17:00','2023-08-08 16:17:00','\0'),
(175737908177801216,'西瓜群','http://localhost:9999/resource/image/e90bbbb0-6fec-420a-a5af-a52200567316.webp','喜欢运动的小孩子',289,'2023-08-22 15:25:47','2023-08-22 15:25:47','\0'),
(175738940119191552,'乐此不疲','http://localhost:9999/resource/image/263cd5ae-cddd-4002-8850-d5d97a3e1b42.webp','一响贪欢，终是难解！',290,'2023-08-22 15:29:53','2023-08-22 15:29:53','\0');

/*Table structure for table `db_group_message` */

DROP TABLE IF EXISTS `db_group_message`;

CREATE TABLE `db_group_message` (
  `id` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `send_time` varchar(25) DEFAULT NULL,
  `content` varchar(600) DEFAULT NULL,
  `message_type` varchar(10) DEFAULT NULL,
  `is_withdrawn` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_group_message` */

insert  into `db_group_message`(`id`,`group_id`,`sender_id`,`sender_name`,`avatar`,`send_time`,`content`,`message_type`,`is_withdrawn`,`create_time`,`update_time`,`is_deleted`) values 
(175738021155573760,175737908177801216,289,'Jie','http://localhost:9999/resource/image/avatar-of-user-289.jpg','2023-08-22 15:26:14','有人吗','text',0,'2023-08-22 15:26:14','2023-08-22 15:26:14','\0'),
(175738057868316672,175737908177801216,290,'dd','http://localhost:9999/resource/image/avatar-of-user-290.jpg','2023-08-22 15:26:22','你好','text',0,'2023-08-22 15:26:22','2023-08-22 15:26:22','\0'),
(175738114692747264,175737908177801216,289,'Jie','http://localhost:9999/resource/image/avatar-of-user-289.jpg','2023-08-22 15:26:36','http://localhost:9999/resource/image/38def152-38fc-48b5-8fbf-d7ddf420b6e3.jpg','image',0,'2023-08-22 15:26:36','2023-08-22 15:26:36','\0'),
(175738267491241984,175737908177801216,289,'Jie',NULL,'2023-08-22 15:27:12',NULL,'system',0,'2023-08-22 15:27:12','2023-08-22 15:27:12','\0');

/*Table structure for table `db_log` */

DROP TABLE IF EXISTS `db_log`;

CREATE TABLE `db_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `information` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=848 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_log` */

insert  into `db_log`(`id`,`type`,`user_id`,`information`,`create_time`,`update_time`,`is_deleted`) values 
(782,'user-register',NULL,'邮箱:44@163.com注册账号，昵称为：Jie','2023-07-28 15:45:25','2023-07-28 15:45:25','\0'),
(783,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-28 15:45:56','2023-07-28 15:45:56','\0'),
(784,'user-register',NULL,'邮箱:wu@11.com注册账号，昵称为：dd','2023-07-28 15:49:24','2023-07-28 15:49:24','\0'),
(785,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-07-28 15:49:29','2023-07-28 15:49:29','\0'),
(786,'user-register',NULL,'邮箱:mimm@ww.com注册账号，昵称为：玛雅','2023-07-28 15:59:57','2023-07-28 15:59:57','\0'),
(787,'user-login',291,'邮箱:mimm@ww.com，昵称为：玛雅','2023-07-28 16:00:01','2023-07-28 16:00:01','\0'),
(788,'user-register',NULL,'邮箱:example@example.com注册账号，昵称为：三八四','2023-07-30 15:44:11','2023-07-30 15:44:11','\0'),
(789,'user-login',292,'邮箱:example@example.com，昵称为：三八四','2023-07-30 16:08:42','2023-07-30 16:08:42','\0'),
(790,'user-login',292,'邮箱:example@example.com，昵称为：三八四','2023-07-30 16:09:47','2023-07-30 16:09:47','\0'),
(791,'user-login',292,'邮箱:example@example.com，昵称为：三八四','2023-07-30 16:21:25','2023-07-30 16:21:25','\0'),
(792,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 13:40:45','2023-07-31 13:40:45','\0'),
(793,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 13:47:03','2023-07-31 13:47:03','\0'),
(794,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 13:57:13','2023-07-31 13:57:13','\0'),
(795,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 14:03:57','2023-07-31 14:03:57','\0'),
(796,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 14:04:26','2023-07-31 14:04:26','\0'),
(797,'user-register',NULL,'邮箱:11@qq.com注册账号，昵称为：账号','2023-07-31 15:10:46','2023-07-31 15:10:46','\0'),
(798,'user-register',NULL,'邮箱:88@qq.com注册账号，昵称为：账号88@qq.com','2023-07-31 15:12:17','2023-07-31 15:12:17','\0'),
(799,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 15:27:31','2023-07-31 15:27:31','\0'),
(800,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 16:12:41','2023-07-31 16:12:41','\0'),
(801,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 16:16:58','2023-07-31 16:16:58','\0'),
(802,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-07-31 16:46:11','2023-07-31 16:46:11','\0'),
(803,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-07-31 16:59:27','2023-07-31 16:59:27','\0'),
(804,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-01 10:10:39','2023-08-01 10:10:39','\0'),
(805,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-01 10:19:50','2023-08-01 10:19:50','\0'),
(806,'user-register',NULL,'邮箱:wu@163.com注册账号，昵称为：账号wu@163.com','2023-08-01 10:47:48','2023-08-01 10:47:48','\0'),
(807,'user-login',295,'邮箱:wu@163.com，昵称为：账号wu@163.com','2023-08-01 10:48:02','2023-08-01 10:48:02','\0'),
(808,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-01 16:58:04','2023-08-01 16:58:04','\0'),
(809,'user-login',295,'邮箱:wu@163.com，昵称为：账号wu@163.com','2023-08-04 16:50:16','2023-08-04 16:50:16','\0'),
(810,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-05 15:58:59','2023-08-05 15:58:59','\0'),
(811,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-05 15:59:35','2023-08-05 15:59:35','\0'),
(812,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-05 16:05:31','2023-08-05 16:05:31','\0'),
(813,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-05 16:10:16','2023-08-05 16:10:16','\0'),
(814,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-05 16:12:22','2023-08-05 16:12:22','\0'),
(815,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-05 16:13:21','2023-08-05 16:13:21','\0'),
(816,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-05 21:58:14','2023-08-05 21:58:14','\0'),
(817,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-05 21:58:27','2023-08-05 21:58:27','\0'),
(818,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-05 22:41:12','2023-08-05 22:41:12','\0'),
(819,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-05 22:41:21','2023-08-05 22:41:21','\0'),
(820,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-06 09:48:37','2023-08-06 09:48:37','\0'),
(821,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-06 09:48:49','2023-08-06 09:48:49','\0'),
(822,'user-login',295,'邮箱:wu@163.com，昵称为：账号wu@163.com','2023-08-06 10:00:14','2023-08-06 10:00:14','\0'),
(823,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-06 11:02:54','2023-08-06 11:02:54','\0'),
(824,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-06 11:03:13','2023-08-06 11:03:13','\0'),
(825,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-06 11:12:11','2023-08-06 11:12:11','\0'),
(826,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-06 11:12:20','2023-08-06 11:12:20','\0'),
(827,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-06 15:27:42','2023-08-06 15:27:42','\0'),
(828,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-06 15:27:58','2023-08-06 15:27:58','\0'),
(829,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-06 15:35:03','2023-08-06 15:35:03','\0'),
(830,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-06 15:35:17','2023-08-06 15:35:17','\0'),
(831,'user-register',NULL,'邮箱:bi@qq.com注册账号，昵称为：账号bi@qq.com','2023-08-06 15:38:04','2023-08-06 15:38:04','\0'),
(832,'user-login',296,'邮箱:bi@qq.com，昵称为：账号bi@qq.com','2023-08-06 15:38:24','2023-08-06 15:38:24','\0'),
(833,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-07 14:46:35','2023-08-07 14:46:35','\0'),
(834,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-07 14:56:56','2023-08-07 14:56:56','\0'),
(835,'user-register',NULL,'邮箱:ee@163.com注册账号，昵称为：米奇','2023-08-07 15:01:13','2023-08-07 15:01:13','\0'),
(836,'user-login',297,'邮箱:ee@163.com，昵称为：米奇','2023-08-07 15:01:14','2023-08-07 15:01:14','\0'),
(837,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-14 10:57:04','2023-08-14 10:57:04','\0'),
(838,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-14 10:57:54','2023-08-14 10:57:54','\0'),
(839,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-18 14:28:40','2023-08-18 14:28:40','\0'),
(840,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-18 14:31:57','2023-08-18 14:31:57','\0'),
(841,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-18 21:49:11','2023-08-18 21:49:11','\0'),
(842,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-19 09:44:14','2023-08-19 09:44:14','\0'),
(843,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-19 09:52:35','2023-08-19 09:52:35','\0'),
(844,'user-register',NULL,'邮箱:mimi@163.com注册账号，昵称为：账号mimi@163.com','2023-08-21 10:26:16','2023-08-21 10:26:16','\0'),
(845,'user-login',298,'邮箱:mimi@163.com，昵称为：账号mimi@163.com','2023-08-21 10:28:01','2023-08-21 10:28:01','\0'),
(846,'user-login',289,'邮箱:44@163.com，昵称为：Jie','2023-08-22 15:01:15','2023-08-22 15:01:15','\0'),
(847,'user-login',290,'邮箱:wu@11.com，昵称为：dd','2023-08-22 15:01:28','2023-08-22 15:01:28','\0');

/*Table structure for table `db_member` */

DROP TABLE IF EXISTS `db_member`;

CREATE TABLE `db_member` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `unread` int(11) DEFAULT NULL,
  `last_message` varchar(255) DEFAULT NULL,
  `is_hidden` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=341 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_member` */

insert  into `db_member`(`id`,`member_id`,`group_id`,`unread`,`last_message`,`is_hidden`,`is_deleted`,`create_time`,`update_time`) values 
(338,289,175737908177801216,4,'Jie:null','0','\0','2023-08-22 15:25:47','2023-08-22 15:27:12'),
(339,290,175737908177801216,4,'Jie:null','0','\0','2023-08-22 15:26:02','2023-08-22 15:27:12'),
(340,290,175738940119191552,0,'你已成功创建群聊','0','\0','2023-08-22 15:29:53','2023-08-22 15:29:53');

/*Table structure for table `db_message` */

DROP TABLE IF EXISTS `db_message`;

CREATE TABLE `db_message` (
  `id` bigint(20) NOT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `receiver_id` bigint(255) DEFAULT NULL,
  `send_time` varchar(25) DEFAULT NULL,
  `content` varchar(600) DEFAULT NULL,
  `message_type` varchar(10) DEFAULT NULL,
  `is_withdrawn` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_message` */

insert  into `db_message`(`id`,`session_id`,`sender_id`,`receiver_id`,`send_time`,`content`,`message_type`,`is_withdrawn`,`create_time`,`update_time`,`is_deleted`) values 
(175732239387201536,166684400011055104,289,290,'2023-08-22 15:03:15','你好','text',0,'2023-08-22 15:03:15','2023-08-22 15:03:15','\0'),
(175732265706459136,166684400011055104,289,290,'2023-08-22 15:03:21','/static/emoji/face-laughing.png','emoji',0,'2023-08-22 15:03:21','2023-08-22 15:03:21','\0'),
(175737470351183872,166684400011055104,289,290,'2023-08-22 15:24:02','/static/emoji/face-open.png','emoji',0,'2023-08-22 15:24:02','2023-08-22 15:24:02','\0'),
(175737541604020224,166684400011055104,289,290,'2023-08-22 15:24:19','http://localhost:9999/resource/image/e8869d7e-36bb-41b8-b1a7-c35c5d371df0.png','image',0,'2023-08-22 15:24:19','2023-08-22 15:24:19','\0'),
(175737586906697728,166684400011055104,290,289,'2023-08-22 15:24:30','http://localhost:9999/resource/image/e9700432-b5bb-4acc-81d6-0a9a4a66ff0d.jpg','image',0,'2023-08-22 15:24:30','2023-08-22 15:24:30','\0'),
(175737643668213760,166684400011055104,290,289,'2023-08-22 15:24:44','http://localhost:9999/resource/video/e9a46eff-c7c0-45b2-95d3-6cd699406a86.mp4','video',0,'2023-08-22 15:24:44','2023-08-22 15:24:44','\0'),
(175745997555765248,166684400011055104,289,290,'2023-08-22 15:57:55','gg','text',0,'2023-08-22 15:57:55','2023-08-22 15:57:55','\0'),
(175746039565914112,166684400011055104,290,289,'2023-08-22 15:58:05','gg','text',0,'2023-08-22 15:58:05','2023-08-22 15:58:05','\0'),
(175747850762850304,166684400011055104,290,289,'2023-08-22 16:05:17','http://localhost:9999/resource/video/15d7e183-a999-42f6-8232-a165606b348a.mp4','video',0,'2023-08-22 16:05:17','2023-08-22 16:05:17','\0'),
(175747982220726272,166684400011055104,289,290,'2023-08-22 16:05:49','http://localhost:9999/resource/image/b455f9a6-3a47-432d-aef4-d2abb9c15eaf.webp','image',0,'2023-08-22 16:05:49','2023-08-22 16:05:49','\0');

/*Table structure for table `db_notice` */

DROP TABLE IF EXISTS `db_notice`;

CREATE TABLE `db_notice` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` bigint(20) DEFAULT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `information` tinytext,
  `is_read` bit(1) DEFAULT b'0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_notice` */

insert  into `db_notice`(`id`,`sender_id`,`receiver_id`,`type`,`information`,`is_read`,`create_time`,`update_time`,`is_deleted`) values 
(116,290,289,'person-apply','{\"senderId\":\"290\",\"senderName\":\"dd\",\"receiverId\":\"289\",\"notation\":\"Jie\",\"applyMessage\":\"你好!我是dd\"}','','2023-07-28 15:50:17','2023-07-28 15:50:17','\0'),
(117,291,290,'person-apply','{\"senderId\":\"291\",\"senderName\":\"玛雅\",\"receiverId\":\"290\",\"notation\":\"dd\",\"applyMessage\":\"你好!我是玛雅\"}','','2023-07-28 16:09:16','2023-07-28 16:09:16','\0'),
(118,289,NULL,'person-apply','{\"senderId\":\"289\",\"senderName\":\"Jie\",\"notation\":\"ii\",\"applyMessage\":\"你好!我是Jie\"}','\0','2023-08-19 09:50:28','2023-08-19 09:50:28','\0');

/*Table structure for table `db_user` */

DROP TABLE IF EXISTS `db_user`;

CREATE TABLE `db_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `background` varchar(255) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `is_activated` bit(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=299 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `db_user` */

insert  into `db_user`(`id`,`email`,`nickname`,`avatar`,`password`,`gender`,`background`,`birthday`,`introduction`,`is_activated`,`create_time`,`update_time`,`is_deleted`) values 
(289,'44@163.com','Jie','http://localhost:9999/resource/image/avatar-of-user-289.jpg','8683c984d931b29cd6e6f8369e3ecc06',1,'http://localhost:9999/resource/image/avatar-of-user-289.jpg','2023-07-28 15:45:25','','','2023-07-28 15:45:25','2023-07-28 15:47:21','\0'),
(290,'wu@11.com','dd','http://localhost:9999/resource/image/avatar-of-user-290.jpg','8683c984d931b29cd6e6f8369e3ecc06',1,'http://localhost:9999/resource/image/avatar-of-user-290.jpg','2023-07-28 15:49:24','','','2023-07-28 15:49:24','2023-07-28 15:49:38','\0'),
(291,'mimm@ww.com','玛雅','http://localhost:9999/resource/image/avatar-of-user-291.png','8683c984d931b29cd6e6f8369e3ecc06',0,'http://localhost:9999/resource/image/avatar-of-user-291.png','2023-07-28 15:59:57','','','2023-07-28 15:59:57','2023-07-28 16:00:18','\0'),
(292,'example@example.com','三八四','','50bb80be0198ea92ec80ad0dcf7ad2ed',1,'','2023-07-30 15:44:11','','','2023-07-30 15:44:11','2023-07-30 15:44:11','\0'),
(293,'11@qq.com','账号','','8683c984d931b29cd6e6f8369e3ecc06',1,'','2023-07-31 15:10:46','','','2023-07-31 15:10:46','2023-07-31 15:10:46','\0'),
(294,'88@qq.com','账号88@qq.com','','8683c984d931b29cd6e6f8369e3ecc06',1,'','2023-07-31 15:12:17','','','2023-07-31 15:12:17','2023-07-31 15:12:17','\0'),
(295,'wu@163.com','账号wu@163.com','https://gd-hbimg.huaban.com/9f305612b17410f7090cd7312a56c7c7e6dacf74efeb-FmaGYX_fw658webp','8683c984d931b29cd6e6f8369e3ecc06',1,'https://gd-hbimg.huaban.com/9f305612b17410f7090cd7312a56c7c7e6dacf74efeb-FmaGYX_fw658webp','2023-08-01 10:47:48','','','2023-08-01 10:47:48','2023-08-01 10:47:48','\0'),
(296,'bi@qq.com','账号bi@qq.com','','312b0eacc10febdef45388e50e965741',1,'','2023-08-06 15:38:04','','','2023-08-06 15:38:04','2023-08-06 15:38:04','\0'),
(297,'ee@163.com','米奇','http://localhost:9999/resource/image/avatar-of-user-297.png','8683c984d931b29cd6e6f8369e3ecc06',1,'http://localhost:9999/resource/image/avatar-of-user-297.png','2023-08-07 15:01:13','','','2023-08-07 15:01:13','2023-08-07 15:01:32','\0'),
(298,'mimi@163.com','账号mimi@163.com','','8683c984d931b29cd6e6f8369e3ecc06',1,'','2023-08-21 10:26:16','','','2023-08-21 10:26:16','2023-08-21 10:26:16','\0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
