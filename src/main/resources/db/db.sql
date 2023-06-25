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

CREATE TABLE IF NOT EXISTS `photos_categories` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL UNIQUE,
  `description` text NULL,
  `parent_id` int,
  `owner_id` int NOT NULL,
  `date_add` datetime NOT NULL,
  `date_edit` datetime,
  `date_delete` datetime,
    FOREIGN KEY (`parent_id`) REFERENCES `photos_categories` (`id`),
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `photos` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL UNIQUE,
  `path` varchar(255) NOT NULL,
  `description` text NULL,
  `category_id` int,
  `owner_id` int NOT NULL,
  `date_add` datetime NOT NULL,
  `date_edit` datetime,
  `date_delete` datetime,
    FOREIGN KEY (`category_id`) REFERENCES `photos_categories` (`id`),
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS photos_comments
(
    id       int auto_increment,
    photo_id int       not null,
    user_id  int       not null,
    comment  TEXT      not null,
    date_add timestamp not null,
    constraint photos_comments_pk
        primary key (id),
    constraint photos_comments_photos_id_fk
        foreign key (photo_id) references photos (id),
    constraint photos_comments_users_id_fk
        foreign key (user_id) references users (id)
);

CREATE TABLE IF NOT EXISTS history_actions
(
    id       int auto_increment,
    user_id  int          not null,
    path     varchar(255) not null,
    date_add timestamp    not null,
    constraint history_actions_pk
        primary key (id),
    constraint history_actions_users_id_fk
        foreign key (user_id) references users (id)
);