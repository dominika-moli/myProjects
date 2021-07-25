--role
insert into Role(id, name) values ('S','STUDENT');
insert into Role(id, name) values ('A','ADMINISTRATOR');
insert into Role(id, name) values ('M','MANAGER');

--certification

insert into Certification(id,name,url,price, currency, version) values (10000,'IBM IT Essentials','https://www.ibm.com',120.90, 'EUR', 0);
insert into certification(id,name,url, price, currency, createdBy, createdDate, lastModifiedBy, lastModifiedDate, version)
values (10001,'Java 11','https://oracle.com', 70, 'EUR', 'Thats me', sysdate, 'Thats me', sysdate, 0);

--certification
insert into certification(id,name,url, price, currency, version) values (10002,'Spring Boot','https://spring.io', 149.99, 'EUR', 0);
insert into certification(id,name,url, price, currency, version) values (10003,'SAP Admin','https://spa.com', 0, 'EUR', 0);
insert into certification(id,name,url, price, currency, version) values (10004,'Oracle 12c Admin','https://oracle.com', null, 'EUR', 0);
insert into certification(id,name,url, price, currency, version) values (10005,'OOP in Java','https://oracle.com', 15.70, 'EUR', 0);
insert into certification(id,name,url, price, currency, version) values (10006,'C++ 17','https://cppreference.com', 29.9, 'EUR', 0);

--user
insert into user(id,name,surname,email, role_id) values(10011,'Elon','Musk','emusk@tesla.com', 'M');
insert into user(id,name,surname,email, role_id) values(10012,'Tom','Paul','tpaul@tesla.com', 'S');
insert into user(id,name,surname,email, role_id) values(10013,'Tom','Black','tblack@tesla.com', 'S');
insert into user(id,name,surname,email, role_id) values(10014,'Zac','Klop','zklop@tesla.com', 'S');
insert into user(id,name,surname,email, role_id) values(10015,'Tom','Rodgers','trodgers@tesla.com', 'S');
insert into user(id,name,surname,email, role_id) values(10016,'Someone','Else','else@gmail.com', 'S');
insert into user(id,name,surname,email, role_id) values(10017,'Someone','New','new@gmail.com', 'S');
insert into user(id,name,surname,email, role_id) values(10018,'Other','Guy','other.guy@gmail.com', 'S');
insert into user(id,name,surname,email, role_id) values(10019,'Other','Girl','other.girl@gmail.com', 'S');

--voucher
insert into voucher(id,state,vouchercode,validUntil,certification_id) values (10020,'ACTIVE','VC1234',TO_DATE('2021-07-29', 'YYYY-MM-DD'),10001);
insert into voucher(id,state,vouchercode,validUntil,certification_id) values (10030,'ACTIVE','VC1234',TO_DATE('2021-07-23', 'YYYY-MM-DD'),10001);
insert into voucher(id,state,vouchercode,validUntil,certification_id) values (10040,'ACTIVE','VC1234',TO_DATE('2021-07-26', 'YYYY-MM-DD'),10003);
insert into voucher(id,state,vouchercode,validUntil,certification_id) values (10050,'PENDING','VC1234',TO_DATE('2021-07-26', 'YYYY-MM-DD'),10001);
insert into voucher(id,state,vouchercode,validUntil,certification_id) values (10060,'PENDING','VC1238',TO_DATE('2021-07-26', 'YYYY-MM-DD'),10003);
update voucher set user_id=10012 where id = 10020;
update voucher set user_id=10012 where id = 10050;


--skill
insert into skill(id,name,desc) values (20001, 'Junior Programmer','Junior Programmer');
insert into skill(id,name,desc) values (20002, 'Java Developer','Java Developer');
insert into skill(id,name,desc) values (20003, 'C++ Developer','C++ Developer');
insert into skill(id,name,desc) values (20004, 'Web Developer','Web Developer');
insert into skill(id,name,desc) values (20005, 'Python Developer','Python Developer');
insert into skill(id,name,desc) values (20006, 'JavaScript Developer','JavaScript Developer');
insert into skill(id,name,desc) values (20007, 'Backend Developer','Backend Developer');
insert into skill(id,name,desc) values (20008, 'Frontend Developer','Frontend Developer');
insert into skill(id,name,desc) values (20009, 'Fullstack Developer','Fullstack Developer');
insert into skill(id,name,desc) values (20010, 'Mobile Developer','Mobile Developer');
insert into skill(id,name,desc) values (20011, 'Embedded Developer','Embedded Developer');
insert into skill(id,name,desc) values (20012, 'Data Scientist','Data Scientist');
insert into skill(id,name,desc) values (20013, 'Test specialist','Test specialist');
insert into skill(id,name,desc) values (20014, 'Oracle database 12c administrator','Oracle database 12c administrator');
insert into skill(id,name,desc) values (20015, 'SAP administration','SAP administration');
insert into skill(id,name,desc) values (20016, 'Database Designer','Database Designer');
insert into skill(id,name,desc) values (20017, 'Database Developer','Database Developer');

--certificatin skill
insert into certification_skill(certification_id, skill_id) values (10000,20001);
insert into certification_skill(certification_id, skill_id) values (10000,20013);
insert into certification_skill(certification_id, skill_id) values (10000,20002);
insert into certification_skill(certification_id, skill_id) values (10001,20001);
insert into certification_skill(certification_id, skill_id) values (10001,20002);
insert into certification_skill(certification_id, skill_id) values (10002,20001);
insert into certification_skill(certification_id, skill_id) values (10002,20002);
insert into certification_skill(certification_id, skill_id) values (10002,20004);
insert into certification_skill(certification_id, skill_id) values (10002,20007);
insert into certification_skill(certification_id, skill_id) values (10003,20015);
insert into certification_skill(certification_id, skill_id) values (10004,20014);
insert into certification_skill(certification_id, skill_id) values (10005,20001);
insert into certification_skill(certification_id, skill_id) values (10006,20001);
insert into certification_skill(certification_id, skill_id) values (10006,20003);
insert into certification_skill(certification_id, skill_id) values (10006,20011);

 --moje testovacie data na rolu
insert into user(id,name,surname,email, role_id) values(10050,'Dominika','Moli','domca.moly@gmail.com', 'S');
insert into user(id,name,surname,email, role_id) values(10060,'Tomo','Halgi','tomo@tesla.com', 'A');
insert into user(id,name,surname,email, role_id) values(10070,'Jozo','Hrasko','jozo@tesla.com', 'S');
insert into user(id,name,surname,email, role_id) values(10080,'Fero','Mrkva','fero@tesla.com', 'S');
insert into user(id,name,surname,email, role_id) values(10090,'Milan','Milo','milo@tesla.com', 'S');
insert into user(id,name,surname,email, role_id) values(10100,'Peter','Pitr','pitr@tesla.com', 'M');
update voucher set user_id=10012 where id = 10020;
update voucher set user_id=10050 where id = 10060;
update voucher set user_id=10050 where id = 10030;
update voucher set user_id=10050 where id = 10040;
