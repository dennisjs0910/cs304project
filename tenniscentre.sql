drop table Lesson;
drop table EmployeesWorkAt;
drop table Coach;
drop table TennisCentre;
drop table TennisCourt;
drop table LessonCourt;
drop table ReservableCourt;
drop table Enrolled_In;
drop table Customer;
drop table Reserve;

CREATE TABLE Reserve(
CID INTEGER NOT NULL,
CourtID INTEGER NOT NULL,
Date DATE,
EndTime TIME,
StartTIme TIME,
PRIMARY KEY (CID, CourtID),
FOREIGN KEY (CID) REFERENCES Customer ON DELETE CASCADE, ON UPDATE CASCADE,
FOREIGN KEY (CourtID) REFERENCES ReservableCourt ON DELETE CASCADE, ON UPDATE CASCADE);

grant select on Reserve to public;

CREATE TABLE Customer(
CID INTEGER,
CCNumber CHAR(16),
Phone CHAR(10),
Name CHAR(20),
Address CHAR(50),
PRIMARY KEY(CID));

grant select on Customer to public;

CREATE TABLE Enrolled_In (
LID CHAR (6), NOT NULL,
CID INTEGER, NOT NULL,
PRIMARY KEY (LID, CID),
FOREIGN KEY(LID) REFERENCES Lesson,  ON DELETE CASCADE, ON UPDATE CASCADE,
FOREIGN KEY(CID) REFERENCES Customer, ON DELETE CASCADE,
ON UPDATE CASCADE);

grant select on Enrolled_In to puclic;

CREATE TABLE ReservableCourt(
CourtID INTEGER not null,
PRIMARY KEY (CourtID),
FOREIGN KEY (CourtID) REFERENCES TennisCourt ON DELETE CASCADE ON UPDATE CASCADE);

grant select on ReservableCourt to public;

CREATE TABLE LessonCourt(
CourtID INTEGER not null,
PRIMARY KEY (CourtID),
FOREIGN KEY (CourtID) REFERENCES TennisCourt ON DELETE CASCADE, ON UPDATE CASCADE);

grant select on LessonCourt to public;

CREATE TABLE TennisCourt(
CourtID INTEGER not null,
Suface_type CHAR(20) not null,
CentreID CHAR(3) not null,
PRIMARY KEY (CourtID),
FOREIGN KEY (CentreID) REFERENCES TennisCentre ON DELETE CASCADE ON UPDATE CASCADE);

grant select on TennisCourt to public;

CREATE TABLE TennisCentre (
CentreID CHAR(3) not null,
Address CHAR(50) not null,
PRIMARY KEY (CentreID),
UNIQUE (Address));

grant select on TennisCentre to public;

CREATE TABLE Coach (
SIN CHAR(9) not null,
Certification_ID CHAR(9),
PRIMARY KEY (SIN),
UNIQUE (Certification_ID),
FOREIGN KEY (SIN) REFERENCES EmployeesWorkAt ON DELETE CASCADE,ON UPDATE CASCADE);

grant select on Coach to public;

CREATE TABLE EmployeesWorkAt (
SIN CHAR (9) not null,
Phone CHAR(10),
Name CHAR(20) not null,
Address CHAR(50),
CentreID CHAR(3) NOT NULL,
PRIMARY KEY (SIN),
FOREIGN KEY (CentreID) REFERENCES TennisCentre ON DELETE NO ACTION, ON UPDATE CASCADE);

grant select on EmployeesWorkAt to public;

CREATE TABLE Lesson (
lid CHAR(6),
courtid CHAR(9),
sin CHAR(9),
l_level CHAR(3),
PRIMARY KEY (LID));

grant select on Lesson to public;


insert into EmployeesWorkAt values ('101010101','6046667777','Maria Sharapova','21 Jump St., Burnaby BC','001');
insert into EmployeesWorkAt values ('202020202','7789322387','Andre Agassi','123 Fake St., Richmond BC','001');
insert into EmployeesWorkAt values ('303030303','6045527834','Trainer Kevin','5555 Viridian Rd, Vancouver BC','002');
insert into EmployeesWorkAt values ('404040404','6041231234','Roger Federer','77 Nadal St., Vancouver BC','001');
insert into EmployeesWorkAt values ('505050505','6045513214','Andy Roddick','33 Big Mac Ave., Vancouver BC','003');
insert into EmployeesWorkAt values ('606060606','6045523213','Andy Murray','300 English St., Vancouver BC','004');
insert into EmployeesWorkAt values ('707070707','7789119111','Milos Raonic','344 Katharine Blvd., Surrey BC','005');

insert into Coach values ('303030303','123456789');
insert into Coach values ('404040404','000000001');
insert into Coach values ('505050505','234234234');
insert into Coach values ('606060606','253253253');
insert into Coach values ('202020202','098098098');

insert into TennisCentre values ('001','5454 US Open Lane, Richmond BC');
insert into TennisCentre values ('002','602 Fuzzy Ball St., Vancouver BC');
insert into TennisCentre values ('003','123 Dennis St., Coquitlam BC');
insert into TennisCentre values ('004','234 Lucas Ave., Burnaby BC');
insert into TennisCentre values ('005','345 Katharine Blvd., Surrey BC');

insert into TennisCourt values ('000000001','Hard','001');
insert into TennisCourt values ('000000002','Hard','002');
insert into TennisCourt values ('000000003','Hard','003');
insert into TennisCourt values ('000000004','Hard','004');
insert into TennisCourt values ('000000005','Clay','005');
insert into TennisCourt values ('000000006','Clay','001');
insert into TennisCourt values ('000000007','Clay','002');
insert into TennisCourt values ('000000008','Grass','003');
insert into TennisCourt values ('000000009','Grass','004');
insert into TennisCourt values ('000000010','Grass','005');

insert into LessonCourt values ('000000001');
insert into LessonCourt values ('000000002');
insert into LessonCourt values ('000000003');
insert into LessonCourt values ('000000004');
insert into LessonCourt values ('000000005');

insert into ReservableCourt values ('000000006');
insert into ReservableCourt values ('000000007');
insert into ReservableCourt values ('000000008');
insert into ReservableCourt values ('000000009');
insert into ReservableCourt values ('000000010');

insert into Lesson values ('123456', '000000001', '202020202', '001');
insert into Lesson values ('222222', '000000002', '202020202', '001');
insert into Lesson values ('300003', '000000003', '303030303', '002');
insert into Lesson values ('400004', '000000004', '505050505', '003');

insert into Enrolled_In values ('123456','121244');
insert into Enrolled_In values ('123456','121237');
insert into Enrolled_In values ('222222','121244');
insert into Enrolled_In values ('300003','121234');
insert into Enrolled_In values ('400004','123456');
insert into Enrolled_In values ('123456','121244');

insert into Customer values ('123456','1234567890123456','6048221111','Jon Snow','111 W 16th Ave, Vancouver BC');
insert into Customer values ('121234','1234567123213213','6048232133','Tyrian Lannister','41 Wesbrook Mall, Vancouver BC');
insert into Customer values ('121237','1234123123123213','6048223123','Cersei Lannister','123 Westeros Lane, Vancouver BC');
insert into Customer values ('121244','1234523124214212','6048221434','Rob Stark','441 Red Wedding Lane, Vancouver BC');
insert into Customer values ('121247','1234567890232312','6048221567','Ned Stark','4466 W 14th Ave, Vancouver BC');

insert into Reserve values ('123456','000000007','01/01/2017','15:00:00','17:00:00');
insert into Reserve values ('121234','000000009','02/19/2017','9:00:00','11:00:00');
insert into Reserve values ('121237','000000008','02/19/2017','9:00:00','11:00:00');
insert into Reserve values ('121247','000000007','02/22/2017','11:00:00','12:30:00');
insert into Reserve values ('121244','000000010','02/22/2017','13:00:00','15:00:00');

