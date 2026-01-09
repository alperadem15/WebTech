package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.CarRepository;
import com.autovermietung.user.Autovermieter;
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

    // Auto mieten
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

    // Neues Auto hinzufügen
    @PostMapping("/add")
    public Car addCar(@RequestBody Car car) {
        car.setRented(false); // standardmäßig verfügbar
        return carRepository.save(car);
    }
}
