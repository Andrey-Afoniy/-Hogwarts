package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Car;
import ru.hogwarts.school.model.Person;
import ru.hogwarts.school.repository.CarRepository;
import ru.hogwarts.school.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final CarRepository carRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, CarRepository carRepository) {
        this.personRepository = personRepository;
        this.carRepository = carRepository;
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public Person findPerson(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person assignCarToPerson(Long personId, Long carId) {
        Person person = findPerson(personId);
        Car car = carRepository.findById(carId).orElse(null);

        if (person != null && car != null) {
            person.setCar(car);
            return personRepository.save(person);
        }
        return null;
    }

    public Person removeCarFromPerson(Long personId) {
        Person person = findPerson(personId);
        if (person != null) {
            person.setCar(null);
            return personRepository.save(person);
        }
        return null;
    }

    public List<Person> getPeopleWithLicense() {
        return personRepository.findByHasLicenseTrue();
    }

    public List<Person> getPeopleWithCars() {
        return personRepository.findPeopleWithCars();
    }

    public List<Person> getPeopleWithoutCars() {
        return personRepository.findPeopleWithoutCars();
    }

    public List<Person> getAllPeopleWithCarInfo() {
        return personRepository.findAllWithCar();
    }

    public List<Person> getPeopleByCarId(Long carId) {
        return personRepository.findByCarId(carId);
    }
}