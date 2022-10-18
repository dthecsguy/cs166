drop table if exists Place cascade;
drop table if exists Telephone cascade;
drop table if exists Musician cascade;
drop table if exists Album cascade;
drop table if exists Song cascade;
drop table if exists Instrument cascade;
drop table if exists Perform cascade;
drop table if exists Plays cascade;

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
