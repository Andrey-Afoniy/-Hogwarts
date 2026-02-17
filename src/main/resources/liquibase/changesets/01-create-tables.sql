
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    age INTEGER NOT NULL DEFAULT 20,
    email VARCHAR(255) UNIQUE,
    faculty_id BIGINT,
    CONSTRAINT check_student_age CHECK (age >= 16)
);


CREATE TABLE IF NOT EXISTS faculties (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(100) NOT NULL,
    CONSTRAINT unique_faculty_name_color UNIQUE (name, color)
);


CREATE TABLE IF NOT EXISTS avatars (
    id BIGSERIAL PRIMARY KEY,
    file_path VARCHAR(500),
    file_size BIGINT,
    media_type VARCHAR(100),
    preview BYTEA,
    student_id BIGINT,
    CONSTRAINT fk_avatar_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);