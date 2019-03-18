drop table admin;
drop table bookpic;
drop table book;
drop table category;
create table admin(
  id varchar(32) primary key,
  name varchar(32),
  pwd varchar(32)
);

create table category(
  id varchar2(32) primary key,
  name varchar2(32)
);

create table book(
  id varchar2(32) primary key,
  name varchar2(20),
  author varchar2(20),
  price number(7,2),
  publishdate TIMESTAMP,
  caid varchar2(32),
  status number(1),
  quantity number(5),
  del number(1),
  foreign key(caid) REFERENCES  category(id)
);

create table bookpic(
  id varchar2(32) primary key,
  savepath varchar2(200),
  showname varchar2(200),
  iscover number(1),
  bookid varchar2(32),
  foreign key(bookid) REFERENCES  book(id)
);