package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Car;
import ru.hogwarts.school.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car createdCar = carService.createCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        Car car = carService.findCar(id);
        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(car);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/filter/brand/{brand}")
    public ResponseEntity<List<Car>> getCarsByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(carService.findByBrand(brand));
    }

    @GetMapping("/filter/brand-model")
    public ResponseEntity<List<Car>> getCarsByBrandAndModel(
            @RequestParam String brand,
            @RequestParam String model) {
        return ResponseEntity.ok(carService.findByBrandAndModel(brand, model));
    }

    @GetMapping("/filter/price-less-than/{maxPrice}")
    public ResponseEntity<List<Car>> getCarsByMaxPrice(@PathVariable Double maxPrice) {
        return ResponseEntity.ok(carService.findByPriceLessThanEqual(maxPrice));
    }
}