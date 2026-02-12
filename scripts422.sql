CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE people (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL CHECK (age >= 0),
    has_license BOOLEAN DEFAULT FALSE,
    car_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_people_car
        FOREIGN KEY (car_id)
        REFERENCES cars(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE INDEX idx_people_name ON people(name);
CREATE INDEX idx_people_car_id ON people(car_id);
CREATE INDEX idx_cars_brand_model ON cars(brand, model);

INSERT INTO cars (brand, model, price) VALUES
('Toyota', 'Camry', 25000.00),
('Honda', 'Accord', 27000.00),
('Ford', 'Focus', 22000.00),
('BMW', 'X5', 60000.00),
('Tesla', 'Model 3', 45000.00);

INSERT INTO people (name, age, has_license, car_id) VALUES
('Иван Иванов', 25, TRUE, 1),
('Петр Петров', 30, TRUE, 2),
('Мария Сидорова', 28, TRUE, 3),
('Анна Ковалева', 22, FALSE, NULL),
('Сергей Смирнов', 35, TRUE, 4),
('Ольга Новикова', 29, TRUE, 5),
('Дмитрий Волков', 26, FALSE, NULL),
('Екатерина Морозова', 31, TRUE, 2),
('Алексей Павлов', 24, TRUE, 1);