package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Person;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByHasLicenseTrue();

    List<Person> findByAgeGreaterThanEqual(int minAge);

    List<Person> findByCarId(Long carId);

    @Query("SELECT p FROM Person p WHERE p.car IS NOT NULL")
    List<Person> findPeopleWithCars();

    @Query("SELECT p FROM Person p WHERE p.car IS NULL")
    List<Person> findPeopleWithoutCars();

    @Query("SELECT p FROM Person p LEFT JOIN p.car c")
    List<Person> findAllWithCar();
}