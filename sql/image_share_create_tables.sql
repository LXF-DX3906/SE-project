/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/12/23		                            */
/*==============================================================*/
SET foreign_key_checks = 0;
drop table if exists comment;
drop table if exists have_picture;
drop table if exists like_picture;
drop table if exists favorite_picture;
drop table if exists follow;
drop table if exists album_picture;
drop table if exists user;
drop table if exists picture;
drop table if exists album;


/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
 	user_id     		int not null auto_increment,
    user_name   		varchar(50),
    pwd         		varchar(50),
    phone       		varchar(20),
    email       		varchar(50),
    sex         		varchar(1),
    birthday    		date,
    head_img    		varchar(50),
    introduction        varchar(300),
    province            varchar(50),
    city                varchar(50),
    primary key(user_id),
    unique (user_name, phone, email)
);
/*==============================================================*/
/* Table: picture                                               */
/*==============================================================*/
create table picture
(
	picture_id			int not null auto_increment,
    position	        varchar(50),
    weight              varchar(20),
    height              varchar(20),
    type_name			varchar(20),
    description			varchar(300),
    primary key(picture_id)
);
/*==============================================================*/
/* Table: album                                                 */
/*==============================================================*/
create table album
(
	album_id  			int not null auto_increment,
    album_name	        varchar(50),
    owner_id            int,
    status              varchar(1),
    description			varchar(300),
    primary key(album_id)
);
/*==============================================================*/
/* Table: comment                                               */
/*==============================================================*/
create table comment
(
	comment_id  		int not null,
    user_id	        	int,
    picture_id          int,
    content  			varchar(300),
    primary key(comment_id),
    foreign key(user_id) references user(user_id) on delete cascade on update cascade,
    foreign key(picture_id) references picture(picture_id) on delete cascade on update cascade
);
/*==============================================================*/
/* Table: have_picture                                          */
/*==============================================================*/
create table have_picture
(
	picture_id			int not null,
    user_id				int,
    primary key(picture_id),
    foreign key(user_id) references user(user_id) on delete cascade on update cascade,
    foreign key(picture_id) references picture(picture_id) on delete cascade on update cascade
);
/*==============================================================*/
/* Table: like_picture                                          */
/*==============================================================*/
create table like_picture
(
	picture_id			int not null,
    user_id				int not null,
    primary key(picture_id, user_id),
    foreign key(user_id) references user(user_id) on delete cascade on update cascade,
    foreign key(picture_id) references picture(picture_id) on delete cascade on update cascade
);
/*==============================================================*/
/* Table: favorite_picture                                      */
/*==============================================================*/
create table favorite_picture
(
	picture_id			int not null,
    user_id				int not null,
    primary key(picture_id, user_id),
    foreign key(user_id) references user(user_id) on delete cascade on update cascade,
    foreign key(picture_id) references picture(picture_id) on delete cascade on update cascade
);
/*==============================================================*/
/* Table: follow                                                */
/*==============================================================*/
create table follow
(
	user_id				int not null,
    following_id		int not null,
    primary key(user_id, following_id),
    foreign key(user_id) references user(user_id)  on delete cascade on update cascade,
	foreign key(following_id) references user(user_id)  on delete cascade on update cascade
);
/*==============================================================*/
/* Table: album_picture                                         */
/*==============================================================*/
create table album_picture
(
	album_id		int not null,
	picture_id		int not null,
    primary key(album_id, picture_id),
	foreign key(album_id) references album(album_id)  on delete cascade on update cascade,
	foreign key(picture_id) references picture(picture_id)  on delete cascade on update cascade
);
SET foreign_key_checks = 1;