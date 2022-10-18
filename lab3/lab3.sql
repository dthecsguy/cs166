drop table if exists Prof cascade;
drop table if exists Dept cascade;
drop table if exists Work_Dept cascade;
drop table if exists Proj cascade;
drop table if exists Grad cascade;
drop table if exists GradG cascade;
drop table if exists GradS cascade;

create table Prof(
  ssn char(11) not null,
  name text,
  age integer,
  rank text,
  specialty text,
  primary key(ssn)
);

create table Dept(
  dno char(5) not null,
  ssn char(11),
  dname text,
  office text,
  primary key(dno),
  foreign key (ssn) references Prof(ssn)
);

create table Work_Dept(
  time_pc integer,
  ssn char(11) not null,
  dno char(5),
  primary key(ssn, dno),
  foreign key(ssn) references Prof(ssn),
  foreign key(dno) references Dept(dno)
);

create table Proj(
  budget float(24),
  start_date date,
  end_date date,
  sponsor text,
  manage char(11) not null,
  work_in char(11) not null,
  pno char(5) not null,
  primary key (pno), 
  foreign key (manage) references Prof(ssn),
  foreign key (work_in) references Prof(ssn)
);

create table Grad(
  ssn char(11) not null,
  deg_pg text,
  name text,
  age integer,
  major text not null,
  primary key (ssn),
  foreign key (major) references Dept (dno)
);

create table GradS(
  ssn char(11) not null,
  primary key (ssn),
  foreign key (ssn) references Grad(ssn)
);

create table GradG(
  ssn char(11) not null,
  advise char(11) not null,
  primary key (ssn),
  foreign key (advise) references GradS(ssn),
  foreign key (ssn) references Grad(ssn)
);


