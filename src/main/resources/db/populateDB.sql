delete from meals;

delete from user_roles;

delete from users;

alter sequence global_seq
  restart with 100000;

insert into users (name, email, password)
values ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

insert into user_roles (role, user_id)
values ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

insert into meals (calories, user_id, datetime,description)
values (400, 100000, '2019-02-04 08:00:00.00',null),
       (400, 100000, '2019-02-04 20:00:00.00',null),
       (300, 100000, '2019-02-05 14:00:00.00', 'description4'),
       (2000, 100000, '2019-02-05 16:00:00.00', 'description5'),
       (2000, 100001, '2019-01-04 08:00:00.00',null),
       (400, 100001, '2019-01-04 20:00:00.00',null),
       (100, 100001, '2019-01-05 10:00:00.00', 'description2'),
       (200, 100001, '2019-01-05 14:00:00.00', 'description4');


