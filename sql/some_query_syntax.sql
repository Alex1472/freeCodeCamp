-- You can use >=, >, <, <= for string.
SELECT * FROM majors WHERE major > 'Game Design'; # all majors that come after 'Game Desing' alphabetically


-- AND, OR
-- AND in WHERE condition more strong then OR.


/* LIKE, NOT LIKE, ILIKE, NOT ILIKE, _, %.
There are two wildcards used in conjunction with the LIKE operator. You can use NOT LIKE, to find things that do not matches a patter:
The percent sign (%)
The underscore (_)
The percent sign represents zero, one, or multiple numbers or characters. The underscore represents a single number or character. */

WHERE salary LIKE '200%' # Finds any values that start with 200
WHERE salary LIKE '%200%' # Finds any values that have 200 in any position
WHERE salary LIKE '_00%' # Finds any values that have 00 in the second and third positions
WHERE salary LIKE '2___3' # Finds any values in a five-digit number that start with 2 and end with 3

SELECT * FROM courses WHERE course ILIKE '%a%'; -- ILIKE - ignore case in pattern (all strings that contain 'a' or 'A')
SELECT * FROM courses WHERE course NOT ILIKE '%a%' AND course LIKE '% %'; -- combine as others conditions. (don't containe 'a' or 'A' and have space)


-- IS NULL, IS NOT NULL;
-- IS NULL - check is a value is null
SELECT * FROM students WHERE gpa IS NULL;


-- ORDER BY, LIMIT
SELECT * FROM students ORDER BY gpa;
SELECT * FROM students ORDER BY gpa DESC;
SELECT * FROM students ORDER BY gpa DESC, first_name; -- order by several columns;
SELECT * FROM students ORDER BY gpa DESC, first_name LIMIT 10;
SELECT * FROM students WHERE gpa IS NOT NULL ORDER BY gpa DESC, first_name LIMIT 10;


-- CEIL, FLOOR, ROUND
-- Rounding to whole number.
SELECT CEIL(AVG(major_id)) FROM students;
SELECT ROUND(AVG(major_id)) FROM students;
SELECT ROUND(AVG(major_id), 5) FROM students; -- several digits after a comma.


-- DISTINCT
SELECT DISTINCT(major_id) FROM students; -- all different not null values.


-- GROUP BY, HAVING
SELECT major_id, COUNT(*) FROM students GROUP BY major_id;
SELECT major_id, MIN(gpa), MAX(gpa) FROM students GROUP BY major_id HAVING MAX(gpa) = 4.0; -- filter groupes
SELECT major_id, COUNT(*) AS number_of_students FROM students GROUP BY major_id; -- use alieses


-- JOIN (INNER JOIN), LEFT JOIN, RIGHT JOIN, FULL JOIN
-- You can use differents joins to match rows in two tables.

-- AS, USING
SELECT s.major_id FROM students AS s FULL JOIN majors AS m ON s.major_id = m.major_id;
SELECT * FROM students FULL JOIN majors USING(major_id); -- you can use USING(column_name) to specify column to join, if column names are the same.


-- You can use a plus sign sum value from different columns
SELECT AVG(winner_goals + opponent_goals) FROM games;

-- UNION, UNION ALL
-- You can use UNION to union results of to queries
SELECT * FROM customers WHERE name >= 'w' UNION SELECT * FROM customers WHERE name <= 'g';
-- UNION do not contains same rows but UNION ALL do


-- You can set default value for a column with DEFAULT keyword.
-- You can don't specify values for this columns when insert rows.
ALTER TABLE bikes ADD COLUMN available BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE rentals ADD COLUMN date_rented DATE NOT NULL DEFAULT NOW();


-- You can clear table with the TRANCATE command
TRUNCATE majors; -- remove all rows from the majors table
TRUNCATE students, courses_majors, majors; -- clear several tables