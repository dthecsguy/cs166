drop table Prof if exists cascade;
drop table Dept if exists cascade;
drop table Work_Dept if exists cascade;

create table Prof(
  ssn char(11) not null,
  name text,
  age integer,
  rank text,
  specialty text,
  primary key (ssn)
);

create table Dept(
  dno char(5) not null,
  dname text,
  office text,
  primary key (dno),
  foreign key (ssn) references Prof(ssn)
);

create table Work_Dept(
  time_pc integer,
  ssn char(11) not null,
  dno char(5),
  primary key (ssn, dno),
  foreign key (ssn) references Prof(ssn),
  foreign key (dno) references Dept(dno)
);
