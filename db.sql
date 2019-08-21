DROP DATABASE IF EXISTS `cuni`;

CREATE DATABASE `cuni`;

USE `cuni`;

CREATE TABLE `article` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` char(100) NOT NULL,
  `body` text NOT NULL,
  `memberId` int(10) UNSIGNED NOT NULL,
  `boardId` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `articleReply` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `articleId` int(10) UNSIGNED NOT NULL,
  `boardId` int(10) UNSIGNED NOT NULL,
  `memberId` int(10) UNSIGNED NOT NULL,
  `body` text NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `board` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` datetime NOT NULL,
  `name` char(100) NOT NULL,
  PRIMARY KEY (`id`)
);

insert  into `board`(`id`,`regDate`,`name`) values 
(1,'2019-05-27 22:54:25','공지사항'),
(2,'2019-05-27 22:54:36','자유게시판');

CREATE TABLE `member` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` datetime NOT NULL,
  `loginId` char(100) NOT NULL,
  `loginPw` char(100) NOT NULL,
  `name` char(100) NOT NULL,
  `emailAuthKey` char(100) NOT NULL,
  `emailAuthStatus` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `delStatus` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `email` char(100) NOT NULL,
  `permissionLevel` int(10) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
);

insert  into `member`(`id`,`regDate`,`loginId`,`loginPw`,`name`,`emailAuthKey`,`emailAuthStatus`,`delStatus`,`email`,`permissionLevel`) values 
(1,'2019-05-27 22:54:25','user1','user1','홍길동','',1,0,'user1@test.com',1),
(2,'2019-05-27 22:54:36','user2','user2','홍길순','',1,0,'user2@test.com',0);
