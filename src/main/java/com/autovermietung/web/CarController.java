package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.user.Autovermieter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://frontendwebtech-oq0k.onrender.com")
@RestController
public class CarController {

    // Mock-Vermieter für Demo
    private final Autovermieter vermieter1 = new Autovermieter(1L, "vermieter1@mail.de", "secret", "Autohaus Müller");
    private final Autovermieter vermieter2 = new Autovermieter(2L, "vermieter2@mail.de", "secret", "Premium Cars GmbH");

    // Mock-Autos (ArrayList, damit wir rented ändern können)
    private final List<Car> cars = new ArrayList<>(List.of(
            new Car(1L, "VW", "Tiguan", 99.99, vermieter1),
            new Car(2L, "Mercedes-Benz", "S500", 350.00, vermieter2),
            new Car(3L, "Tesla", "Model S", 150.00, vermieter1)
    ));

    //  Alle Autos
    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return cars;
    }

    // Autos eines bestimmten Vermieters
    @GetMapping("/cars/vermieter/{id}")
    public List<Car> getCarsByVermieter(@PathVariable Long id) {
        return cars.stream()
                .filter(car -> car.getOwner().getId().equals(id))
                .collect(Collectors.toList());
    }

    // Auto mieten (mock)
    @GetMapping("/cars/rent/{id}")
    public String rentCar(@PathVariable Long id) {
        for (Car car : cars) {
            if (car.getId().equals(id)) {
                if (car.isRented()) {
                    return "Dieses Auto ist bereits vermietet!";
                } else {
                    car.setRented(true);
                    return "Auto " + car.getBrand() + " " + car.getModel() + " wurde gemietet!";
                }
            }
        }
        return "Auto nicht gefunden!";
    }

    // nur verfügbare Autos anzeigen
    @GetMapping("/cars/available")
    public List<Car> getAvailableCars() {
        return cars.stream()
                .filter(car -> !car.isRented())
                .collect(Collectors.toList());
    }
}
