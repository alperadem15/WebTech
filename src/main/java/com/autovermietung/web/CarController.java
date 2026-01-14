package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @GetMapping("/vermieter/{ownerId}")
    public List<Car> getCarsByOwner(@PathVariable Long ownerId) {
        return carRepository.findByOwnerId(ownerId);
    }

    @GetMapping("/available")
    public List<Car> getAvailableCars() {
        return carRepository.findByRentedFalse();
    }

    // ❗ Kein Owner hier → nur speichern
    @PostMapping
    public Car addCar(@RequestBody Car car) {
        car.setRented(false);
        return carRepository.save(car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id, @RequestParam Long ownerId) {
        Car car = carRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Auto nicht gefunden oder nicht dein Auto!"));
        carRepository.delete(car);
    }

    @PatchMapping("/{id}/rented")
    public Car setRented(@PathVariable Long id,
                         @RequestParam boolean rented,
                         @RequestParam Long ownerId) {

        Car car = carRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Auto nicht gefunden oder nicht dein Auto!"));

        car.setRented(rented);
        return carRepository.save(car);
    }
}
