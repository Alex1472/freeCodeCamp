<!-- Open db zip.sqlite3 -->
sqlite3 zip.sqlite3

<!-- Exit from sqlite console -->
.quit

<!-- Make output human readable -->
.headers on
.mode column

<!-- Show all tables -->
.tables

<!-- Show table schema -->
.schema Users

SELECT * FROM Users;

SELECT * FROM Users WHERE email='csev@umich.edu';

SELECT * FROM Users ORDER BY email;

SELECT * FROM Users ORDER BY name DESC;

CREATE TABLE "Users" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, 
    "name" TEXT,
    "email" TEXT
);

INSERT INTO Users (name, email) VALUES ('Kristen', 'kf@umich.edu');
INSERT INTO Users (name, email) VALUES ('Chuck', 'csev@umich.edu');
INSERT INTO Users (name, email) VALUES ('Colleen', 'cvl@umich.edu');
INSERT INTO Users (name, email) VALUES ('Ted', 'ted@umich.edu');
INSERT INTO Users (name, email) VALUES ('Sally', 'a1@umich.edu');

DELETE FROM Users WHERE email='ted@umich.edu';

UPDATE Users SET name="Charles" WHERE email='csev@umich.edu';

DROP TABLE Users;
