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

insert into photos_albums (id, name, description, parent_id, owner_id, date_add, date_edit, date_delete)
values  (2, '12312b', 'sdgsdg', null, 2, '2023-06-18 03:02:37', '2023-06-22 00:20:57', null),
        (3, 'test', 'hdfghfdghj', 2, 1, '2023-06-18 18:03:00', null, null),
        (4, 'cvncvn', 'ghfjghkjghj', 3, 1, '2023-06-18 18:05:45', null, null),
        (6, 'аиавры', 'авсрвыарвкыру', null, 1, '2023-06-18 20:18:57', null, null),
        (7, 'fdhs', 'dfghsdfh', 6, 1, '2023-06-18 20:19:49', null, null),
        (8, 'Станислав Сотников', '', 7, 1, '2023-06-18 20:20:41', null, null),
        (9, '123122', 'sdgsdg', null, 1, '2023-06-22 00:18:50', null, null),
        (10, '12312f', 'sdgsdg', null, 1, '2023-06-22 00:19:42', null, '2023-06-22 00:21:06');

insert into photos_albums_viewers (album_id, user_id)
values  (2, 1),
        (2, 2);

insert into photos (id, name, path, description, album_id, owner_id, date_add, date_edit, date_delete)
values  (1, 'ыпыв213542', '/img/users/1/photos/photo_1.jpg', 'description test  ыпыв213542', null, 1, '2023-06-20 22:37:11', '2023-06-21 00:36:17', null),
        (2, 'Станислав Сотников', '/img/users/1/photos/photo_2.png', 'sdgfxddhdf', 2, 1, '2023-06-21 22:40:20', '2023-06-21 22:45:43', null);

insert into photos_viewers (photo_id, user_id)
values  (1, 1),
        (1, 2);