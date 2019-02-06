DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100000,'04-02-2019 08:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100000,'04-02-2019 10:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100000,'04-02-2019 12:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100000,'04-02-2019 14:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100000,'04-02-2019 16:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100000,'04-02-2019 20:00:00.00');

INSERT INTO meals (calories, user_id,datetime,description) VALUES (100, 100000,'05-02-2019 08:00:00.00','description1');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (200, 100000,'05-02-2019 10:00:00.00','description2');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (100, 100000,'05-02-2019 12:00:00.00','description3');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (300, 100000,'05-02-2019 14:00:00.00','description4');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (200, 100000,'05-02-2019 16:00:00.00','description5');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (100, 100000,'05-02-2019 20:00:00.00','description6');

INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100001,'04-01-2019 08:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100001,'04-01-2019 10:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100001,'04-01-2019 12:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100001,'04-01-2019 14:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100001,'04-01-2019 16:00:00.00');
INSERT INTO meals (calories, user_id,datetime) VALUES (400, 100001,'04-01-2019 20:00:00.00');

INSERT INTO meals (calories, user_id,datetime,description) VALUES (300, 100001,'05-01-2019 08:00:00.00','description1');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (100, 100001,'05-01-2019 10:00:00.00','description2');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (300, 100001,'05-01-2019 12:00:00.00','description3');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (200, 100001,'05-01-2019 14:00:00.00','description4');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (300, 100001,'05-01-2019 16:00:00.00','description5');
INSERT INTO meals (calories, user_id,datetime,description) VALUES (300, 100001,'05-01-2019 20:00:00.00','description6');


