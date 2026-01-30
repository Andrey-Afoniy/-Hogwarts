
SELECT * FROM students
WHERE age BETWEEN 10 AND 20
ORDER BY age;

SELECT name FROM students
ORDER BY name;

SELECT * FROM students
WHERE name ILIKE '%о%'
ORDER BY name;

SELECT * FROM students
WHERE age < id
ORDER BY id;

SELECT * FROM students
ORDER BY age ASC;

SELECT * FROM students
ORDER BY age DESC;

SELECT age, COUNT(*) as student_count
FROM students
GROUP BY age
ORDER BY age;

SELECT AVG(age) as average_age FROM students;


SELECT
    MAX(age) as max_age,
    MIN(age) as min_age,
    MAX(name) keep (dense_rank first ORDER BY age DESC) as oldest_student,
    MAX(name) keep (dense_rank first ORDER BY age ASC) as youngest_student
FROM students;



