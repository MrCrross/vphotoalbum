INSERT INTO `users_roles` (`id`, `tech_name`, `name`)
VALUES (1, 'root', 'Полные права в системе'),
       (2, 'default', 'Стандратная (только свой фотоальбом)');
INSERT INTO `users_params` (`id`, `tech_name`, `name`)
VALUES (1, 'test', 'Тестовое'),
       (2, 'role_viewer', 'Просмотр ролей'),
       (3, 'role_changed', 'Добавление и редактирование ролей'),
       (4, 'role_deleted', 'Удаление ролей'),
       (5, 'user_viewer', 'Просмотр пользователей'),
       (6, 'user_changed', 'Добавление и редактирование пользователей'),
       (7, 'user_deleted', 'Удаление пользователей');
INSERT INTO `users_roles_params` (`role_id`, `param_id`)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7);
INSERT INTO `users` (`id`, `login`, `password`, `fio`, `avatar`, `date_add`, `date_edit`, `date_delete`)
VALUES (1, 'test', 'c9fe854ea69fc0a252340e152864b539b116c36cf1ac419652e1826c3071d5ed', 'test test test',
        '/img/users/1/avatar_1.png', '2023-02-27 21:21:59', '2023-04-18 22:01:35', NULL),
       (2, 'root', 'c9fe854ea69fc0a252340e152864b539b116c36cf1ac419652e1826c3071d5ed', 'root root root',
        '/img/avatar.svg', '2023-03-03 22:28:35', NULL, NULL);
INSERT INTO `users_users_roles` (`role_id`, `user_id`)
VALUES (1, 1),
       (1, 2);