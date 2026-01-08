package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.user.Kunde;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://frontendwebtech-oq0k.onrender.com")
@RequestMapping("/kunde")
public class KundeController {

    // Mock-Liste aller Kunden
    private final List<Kunde> kundeList = new ArrayList<>();
    // Mock-Liste aller Autos
    private final List<Car> cars = new ArrayList<>(); //  aus CarController injecten

    private Long nextKundeId = 1L;

    // Registrierung
    @PostMapping("/register")
    public Kunde register(@RequestBody Kunde kunde) {
        kunde.setId(nextKundeId++);
        kundeList.add(kunde);
        return kunde;
    }

    // Login (mock)
    @PostMapping("/login")
    public String login(@RequestBody Kunde loginRequest) {
        Optional<Kunde> user = kundeList.stream()
                .filter(k -> k.getEmail().equals(loginRequest.getEmail())
                        && k.getPassword().equals(loginRequest.getPassword()))
                .findFirst();
        return user.isPresent() ? "Login erfolgreich!" : "Email oder Passwort falsch!";
    }

    // Auto mieten (mock)
    @PostMapping("/rent/{autoId}")
    public String rentCar(@PathVariable Long autoId) {
        for (Car car : cars) {
            if (car.getId().equals(autoId)) {
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

    // verfügbare Autos anzeigen
    @GetMapping("/cars/available")
    public List<Car> getAvailableCars() {
        return cars.stream()
                .filter(car -> !car.isRented())
                .toList();
    }
}
