drop table if exists Home cascade;
drop table if exists Place cascade;
drop table if exists Telephone cascade;

create table Place(
  address text not null,
  primary key (address)
);

create table Telephone(
  phone_no char(11) not null,
  address text not null,
  foreign key (address) references Place(address),
  primary key (phone_no, address)
);

create table Home(
  phone_no char(11) not null,
  primary key (phone_no),
  foreign key (phone_no) references Telephone(phone_no)
);
