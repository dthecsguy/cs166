drop table if exists Prof cascade;
drop table if exists Dept cascade;
drop table if exists Work_Dept cascade;
drop table if exists Proj cascade;
drop table if exists Grad cascade;
drop table if exists GradG cascade;
drop table if exists GradS cascade;
drop table if exists Work_Proj cascade;

drop table if exists Place cascade;
drop table if exists Telephone cascade;
drop table if exists Musician cascade;
drop table if exists Album cascade;
drop table if exists Song cascade;
drop table if exists Instrument cascade;
drop table if exists Perform cascade;
drop table if exists Plays cascade;

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

create table Work_Proj(
  since date,
  project char(5) not null,
  graduate char(11) not null,
  supervise char(11) not null,
  primary key (project, graduate),
  foreign key (project) references Proj(pno),
  foreign key (graduate) references Grad(ssn),
  foreign key (supervise) references Prof(ssn)
);

create table Place(
  address text not null,
  primary key (address)
);

create table Musician(
  ssn char(11) not null,
  name text,
  primary key (ssn)
);

create table Telephone(
  phone_no char(11) not null,
  home text not null,
  lives char(11),
  foreign key (home) references Place(address),
  foreign key (lives) references Musician(ssn),
  primary key (phone_no, home)
);

create table Instrument(
  instrID char(50) not null,
  key char(1),
  dname text,
  primary key (instrID)
);

create table Album(
  albumIdentifier char(50) not null,
  copyrightDate date,
  speed integer,
  title text,
  producer char(11),
  primary key (albumIdentifier),
  foreign key (producer) references Musician(ssn)
);

create table Song(
  songID char(50)  not null,
  title text,
  suthor text,
  appears char(50),
  primary key (songID),
  foreign key (appears) references Album(albumIdentifier)
);

create table Perform(
  song char(50),
  musician char(11),
  primary key (song, musician),
  foreign key (song) references Song(songID),
  foreign key (musician) references Musician(ssn)
);

create table Plays(
  instrument char(50),
  musician char(11),
  primary key (instrument, musician),
  foreign key (instrument) references Instrument(instrID),
  foreign key (musician) references Musician(ssn)
);
