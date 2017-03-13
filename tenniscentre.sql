drop table Lesson;

CREATE TABLE Lesson (
lid CHAR(6),
courtid CHAR(9),
sin CHAR(9),
l_level CHAR(3),
PRIMARY KEY (LID));

grant select on Lesson to public;

insert into Lesson values ('123456', '111111111', '123123123', '001');
insert into Lesson values ('222222', '111111222', '222000222', '001');
insert into Lesson values ('300003', '111111333', '333000333', '002');
insert into Lesson values ('400004', '111111444', '444000444', '003');
