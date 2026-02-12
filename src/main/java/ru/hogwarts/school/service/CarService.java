package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Car;
import ru.hogwarts.school.repository.CarRepository;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public Car findCar(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<Car> findByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    public List<Car> findByBrandAndModel(String brand, String model) {
        return carRepository.findByBrandAndModel(brand, model);
    }

    public List<Car> findByPriceLessThanEqual(Double maxPrice) {
        return carRepository.findByPriceLessThanEqual(maxPrice);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}