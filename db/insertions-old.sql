USE `module_4_db`;

INSERT INTO `tag` (`id`, `name`)
VALUES
(1, 'tag_1'),
(2, 'tag_2'),
(3, 'tag_3'),
(4, 'tag_4');


INSERT INTO `certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`)
VALUES
(1, 'cert_1 ABC', 'certificate with #1', 100, 7, '2023-04-02T11:30:14.868', '2023-04-02T11:30:14.868'),
(2, 'cert_2 DFG', 'certificate with #2 ABC', 200, 15, '2023-04-03T11:30:14.868', '2023-04-03T11:30:14.868'),
(3, 'cert_3 ZXC', 'certificate with #3 QWE', 300, 30, '2023-04-04T11:30:14.868', '2023-04-04T11:30:14.868'),
(4, 'cert_4 QWE', 'certificate with #4', 400, 45, '2023-04-05T11:30:14.868', '2023-04-05T11:30:14.868');

INSERT INTO `certificate_with_tag` (`id`, `tag_id`, `certificate_id`)
VALUES
(1, 1, 1),
(2, 1, 4),
(3, 2, 3),
(4, 3, 3),
(5, 3, 2),
(6, 3, 4),
(7, 4, 1),
(8, 4, 2);

INSERT INTO `user` (`id`, `name`, `password`, `role`)
VALUES
(1, 'user_1', '$2a$12$nMzx6PbJJ3MCtis0srPthOwJlCmh7dbq/GyffrC52SmGi43tawQ22', 'GUEST'),
(2, 'user_2', '$2a$12$2is1.wOlnR6XHXfgsO/jPu4RyUzSn7WWZ0BK/Y0Ca4NNMcD0RzLhy', 'USER'),
(3, 'Admin', '$2a$12$EugPpe4EBCne9NS6cpEisuwV6DBjE.9gKZXZQQbE3oKDKPCDy.rLO', 'ADMIN');

INSERT INTO `user_order` (`id`, `cost`, `create_date`, `user_id`, `certificate_with_tag_id`)
VALUES
(1, 150.00, '2023-04-02T11:30:14.868', 1, 1),
(2, 215.15, '2023-04-05T11:30:14.868', 2, 4),
(3, 350.50, '2023-04-04T11:30:14.868', 3, 2);

