
ALTER TABLE students
ADD CONSTRAINT fk_student_faculty
FOREIGN KEY (faculty_id)
REFERENCES faculties(id)
ON DELETE SET NULL;


CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;