CREATE SCHEMA IF NOT EXISTS vphotoalbum;

use vphotoalbum;

CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL UNIQUE,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID),
    INDEX (EXPIRY_TIME, PRINCIPAL_NAME)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
   SESSION_PRIMARY_ID CHAR(36) NOT NULL,
   ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
   ATTRIBUTE_BYTES BLOB NOT NULL,
   CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
   CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `users` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `fio` varchar(255) NOT NULL,
  `avatar` varchar(255) DEFAULT '/img/avatar.svg' NOT NULL,
  `date_add` datetime NOT NULL,
  `date_edit` datetime,
  `date_delete` datetime,
    INDEX (`fio`)
);

CREATE TABLE IF NOT EXISTS `users_roles` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `tech_name` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `users_params` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `tech_name` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `users_users_roles` (
  `role_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `user_id`),
    FOREIGN KEY (`role_id`) REFERENCES `users_roles` (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `users_roles_params` (
  `role_id` int NOT NULL,
  `param_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `param_id`),
    FOREIGN KEY (`role_id`) REFERENCES `users_roles` (`id`),
    FOREIGN KEY (`param_id`) REFERENCES `users_params` (`id`)
);

CREATE TABLE IF NOT EXISTS `history_users` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `item_id` int NOT NULL,
  `message` text NOT NULL,
  `date_add` datetime NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `photos_albums` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL UNIQUE,
  `description` text NULL,
  `parent_id` int,
  `owner_id` int NOT NULL,
  `date_add` datetime NOT NULL,
  `date_edit` datetime,
  `date_delete` datetime,
    FOREIGN KEY (`parent_id`) REFERENCES `photos_albums` (`id`),
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `photos` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL UNIQUE,
  `path` varchar(255) NOT NULL,
  `description` text NULL,
  `album_id` int,
  `owner_id` int NOT NULL,
  `date_add` datetime NOT NULL,
  `date_edit` datetime,
  `date_delete` datetime,
    FOREIGN KEY (`album_id`) REFERENCES `photos_albums` (`id`),
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `photos_albums_viewers` (
  `album_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`user_id`, `album_id`),
    FOREIGN KEY (`album_id`) REFERENCES `photos_albums` (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `photos_viewers` (
  `photo_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`user_id`, `photo_id`),
    FOREIGN KEY (`photo_id`) REFERENCES `photos` (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `history_photos` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `item_id` int NOT NULL,
  `message` text NOT NULL,
  `date_add` datetime NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `photos` (`id`)
);

CREATE TABLE IF NOT EXISTS `history_photos_albums` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `item_id` int NOT NULL,
  `message` text NOT NULL,
  `date_add` datetime NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `photos_albums` (`id`)
);

CREATE TABLE IF NOT EXISTS `photos_comments` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `who_add` int NOT NULL,
  `photo_id` int NOT NULL,
  `comment` text NOT NULL,
  `date_add` datetime NOT NULL,
  FOREIGN KEY (`who_add`) REFERENCES `users` (`id`),
    FOREIGN KEY (`photo_id`) REFERENCES `photos` (`id`)
);
