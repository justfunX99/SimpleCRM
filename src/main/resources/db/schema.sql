create table if not exists tb_user (
	ID int not null primary key auto_increment,
	USER_NAME varchar(100),
	PASSWORD varchar(100),
	ROLE varchar(20)
);