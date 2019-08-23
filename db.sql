DROP DATABASE IF EXISTS `cuni`;

CREATE DATABASE `cuni`;

USE `cuni`;

CREATE TABLE `article` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` CHAR(100) NOT NULL,
  `body` TEXT NOT NULL,
  `hit` INT(100) UNSIGNED NOT NULL,
  `memberId` INT(10) UNSIGNED NOT NULL,
  `boardId` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `articleReply` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `articleId` INT(10) UNSIGNED NOT NULL,
  `boardId` INT(10) UNSIGNED NOT NULL,
  `memberId` INT(10) UNSIGNED NOT NULL,
  `body` TEXT NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `board` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL,
  `name` CHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT  INTO `board`(`id`,`regDate`,`name`) VALUES 
(1,'2019-05-27 22:54:25','공지사항'),
(2,'2019-05-27 22:54:36','자유게시판');

CREATE TABLE `member` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL,
  `loginId` CHAR(100) NOT NULL,
  `loginPw` CHAR(100) NOT NULL,
  `name` CHAR(100) NOT NULL,
  `emailAuthKey` CHAR(100) NOT NULL,
  `emailAuthStatus` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0',
  `delStatus` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0',
  `email` char(100) NOT NULL,
  `permissionLevel` int(10) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
);

insert  into `member`(`id`,`regDate`,`loginId`,`loginPw`,`name`,`emailAuthKey`,`emailAuthStatus`,`delStatus`,`email`,`permissionLevel`) values 
(1,'2019-05-27 22:54:25','user1','user1','홍길동','',1,0,'user1@test.com',1),
(2,'2019-05-27 22:54:36','user2','user2','홍길순','',1,0,'user2@test.com',0);
