drop table if exists Airplane cascade;
drop table if exists Model cascade;
drop table if exists Assigned cascade;
drop table if exists Info cascade;
drop table if exists Fix cascade;
drop table if exists Technician cascade;
drop table if exists Test cascade;
drop table if exists Employee cascade;
drop table if exists TrafficController cascade;
drop table if exists Passed cascade;
drop table if exists Partof cascade;
drop table if exists Unionn cascade;
drop table if exists Track cascade;

create table Model(
	modelnumber int not null,
	seatcapacity int,
	weight int,
	fueltype char(11),
	primary key (modelnumber)
);

create table Employee(
	ssn char(11) not null,
	primary key (ssn)
);

create table Airplane(
	registration char(50) not null,
	compname char(50),
	model int not null,
	foreign key (model) references Model(modelnumber),
	primary key (registration)
);

create table Test(
	faa char(50),
	maximum int,
	name char(50),
	primary key (faa)
);

create table Assigned(
	registration char(50) not null,
	test char(50) not null,
	foreign key (registration) references Airplane(registration),
	foreign key (test) references Test(faa),
	primary key (registration, test)
);

create table Technician(
	lastname char(50),
	salary int,
	address char(100),
	phonenumber char(11),
	ssn char(11) not null,
	foreign key (ssn) references Employee(ssn),
	primary key (ssn)
);

create table Track(
	score int not null,
	hours int not null,
	datee date not null,
	faa char(50) not null,
	tech char(11) not null,
	foreign key (faa) references Test(faa),
	foreign key (tech) references Technician(ssn),
	primary key (faa, tech)
);

create table Fix(
	modelnumber int not null,
	tech char(11) not null,
	foreign key (modelnumber) references Model(modelnumber),
	foreign key (tech) references Technician(ssn),
	primary key (modelnumber, tech)  
);

create table TrafficController(
	age int,
	experience int,
	ssn char(11),
	foreign key (ssn) references Employee(ssn),
	primary key (ssn)
);

create table Exam(
	levell int not null,
	duration int not null,
	datee date,
	primary key (levell)
);

create table Passed(
	tc char(11) not null,
	exm int not null,
	foreign key (tc) references TrafficController(ssn),
	foreign key (exm) references Exam(levell),
	primary key (tc, exm)
);

create table Unionn(
	membership int not null,
	primary key (membership)
);

create table Partof(
	emp char(11) not null,
	membership int not null,
	foreign key (emp) references Employee(ssn),
	foreign key (membership) references Unionn(membership),
	primary key (emp, membership)
);
