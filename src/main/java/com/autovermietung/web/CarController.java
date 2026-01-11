package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://frontendwebtech-oq0k.onrender.com")
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    // Alle Autos
    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Autos eines bestimmten Vermieters
    @GetMapping("/vermieter/{ownerId}")
    public List<Car> getCarsByOwner(@PathVariable Long ownerId) {
        return carRepository.findByOwnerId(ownerId);
    }

    // Nur verfügbare Autos
    @GetMapping("/available")
    public List<Car> getAvailableCars() {
        return carRepository.findByRentedFalse();
    }

    // Neues Auto hinzufügen
    @PostMapping
    public Car addCar(@RequestBody Car car) {
        car.setRented(false);
        return carRepository.save(car);
    }

    // Auto löschen (nur Owner darf löschen)
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id, @RequestParam Long ownerId) {
        Car car = carRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Auto nicht gefunden oder nicht dein Auto!"));

        carRepository.delete(car);
    }

    // Mietstatus setzen (nur Owner darf ändern)
    @PatchMapping("/{id}/rented")
    public Car setRented(@PathVariable Long id,
                         @RequestParam boolean rented,
                         @RequestParam Long ownerId) {

        Car car = carRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Auto nicht gefunden oder nicht dein Auto!"));

        car.setRented(rented);
        return carRepository.save(car);
    }

    // Auto mieten (Kunde)
    @PostMapping("/rent/{id}")
    public String rentCar(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> {
                    if (car.isRented()) return "Dieses Auto ist bereits vermietet!";
                    car.setRented(true);
                    carRepository.save(car);
                    return "Auto " + car.getBrand() + " " + car.getModel() + " wurde gemietet!";
                })
                .orElse("Auto nicht gefunden!");
    }
}
