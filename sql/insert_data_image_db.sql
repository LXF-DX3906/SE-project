insert into user(user_name, pwd, phone, email, sex, birthday, head_img, introduction, province, city) VALUES
('name1',123456,13145678901,'576879809@qq.com','0','2007-01-11','pictures/1/avator/1_avator.jpg','this is introduction','Shanghai','Shanghai'),
('name2',123456,13145678901,'576879809@qq.com','1','2007-01-11','pictures/2/avator/2_avator.jpg','this is introduction','Shanghai','Shanghai'),
('name3',123456,13145678901,'576879809@qq.com','1','2007-01-11','pictures/3/avator/3_avator.jpg','this is introduction','Shanghai','Shanghai'),
('name4',123456,13145678901,'576879809@qq.com','1','2007-01-11','pictures/4/avator/4_avator.jpg','this is introduction','Shanghai','Shanghai'),
('name5',123456,13145678901,'576879809@qq.com','1','2007-01-11','pictures/5/avator/5_avator.jpg','this is introduction','Shanghai','Shanghai');

insert into picture(position, weight, height, type_name, description) VALUES
('pictures/1/pictures/1.jpg',4000,800,'艺术','this is description'),
('pictures/1/pictures/2.jpg',3000,600,'艺术','this is description'),
('pictures/1/pictures/3.jpg',2000,500,'艺术','this is description'),
('pictures/1/pictures/4.jpg',4000,800,'艺术','this is description'),
('pictures/1/pictures/5.jpg',4000,800,'艺术','this is description'),
('pictures/2/pictures/6.jpg',5000,900,'艺术','this is description'),
('pictures/2/pictures/7.jpg',4000,800,'艺术','this is description'),
('pictures/2/pictures/8.jpg',2000,400,'艺术','this is description'),
('pictures/2/pictures/9.jpg',4000,800,'艺术','this is description'),
('pictures/2/pictures/10.jpg',4000,800,'艺术','this is description');

insert into album(album_name, owner_id, status, description) VALUES
('name1''s album',1,'1','this is album description'),
('name2''s album',2,'1','this is album description');

insert into album_picture(album_id, picture_id) values
(1,1),
(1,2),
(1,3),
(1,4),
(1,5),
(2,6),
(2,7),
(2,8),
(2,9),
(2,10);

insert into have_picture(picture_id, user_id) VALUES
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),
(6,2),
(7,2),
(8,2),
(9,2),
(10,2);

insert into like_picture(picture_id, user_id) values
(1,2),
(1,3),
(1,4),
(1,5),
(2,2),
(3,2),
(4,2),
(5,2),
(6,1),
(7,1),
(8,1),
(9,1),
(10,1);

insert into favorite_picture(picture_id, user_id) VALUES
(1,2),
(1,3),
(1,4),
(1,5),
(2,2),
(3,2),
(4,2),
(5,2),
(6,1),
(7,1),
(8,1),
(9,1),
(10,1);

insert into comment(user_id, picture_id, content) values
(2,1,'this is name2''s content'),
(3,1,'this is name3''s content'),
(4,1,'this is name4''s content'),
(5,1,'this is name5''s content'),
(2,2,'this is name2''s content'),
(2,3,'this is name2''s content'),
(2,4,'this is name2''s content'),
(1,6,'this is name1''s content'),
(1,7,'this is name1''s content'),
(1,8,'this is name1''s content');

insert into follow(user_id, following_id) VALUES
(1,2),
(3,2),
(4,2),
(2,1),
(3,1),
(4,1),
(5,1);