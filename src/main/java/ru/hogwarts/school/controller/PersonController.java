package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Person;
import ru.hogwarts.school.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person createdPerson = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Long id) {
        Person person = personService.findPerson(id);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(person);
    }

    @PutMapping("/{personId}/assign-car/{carId}")
    public ResponseEntity<Person> assignCarToPerson(
            @PathVariable Long personId,
            @PathVariable Long carId) {
        Person person = personService.assignCarToPerson(personId, carId);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(person);
    }

    @PutMapping("/{personId}/remove-car")
    public ResponseEntity<Person> removeCarFromPerson(@PathVariable Long personId) {
        Person person = personService.removeCarFromPerson(personId);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(person);
    }

    @GetMapping("/with-license")
    public ResponseEntity<List<Person>> getPeopleWithLicense() {
        return ResponseEntity.ok(personService.getPeopleWithLicense());
    }

    @GetMapping("/with-cars")
    public ResponseEntity<List<Person>> getPeopleWithCars() {
        return ResponseEntity.ok(personService.getPeopleWithCars());
    }

    @GetMapping("/without-cars")
    public ResponseEntity<List<Person>> getPeopleWithoutCars() {
        return ResponseEntity.ok(personService.getPeopleWithoutCars());
    }

    @GetMapping("/all-with-car-info")
    public ResponseEntity<List<Person>> getAllPeopleWithCarInfo() {
        return ResponseEntity.ok(personService.getAllPeopleWithCarInfo());
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Person>> getPeopleByCarId(@PathVariable Long carId) {
        return ResponseEntity.ok(personService.getPeopleByCarId(carId));
    }
}