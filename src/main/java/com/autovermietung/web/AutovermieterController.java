package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.user.Autovermieter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://frontendwebtech-oq0k.onrender.com")
@RequestMapping("/vermieter")
public class AutovermieterController {

    // Mock-Liste aller Vermieter
    private final List<Autovermieter> vermieterList = new ArrayList<>();
    // Mock-Liste aller Autos
    private final List<Car> cars = new ArrayList<>();

    private Long nextVermieterId = 1L;
    private Long nextCarId = 1L;

    // Registrierung
    @PostMapping("/register")
    public Autovermieter register(@RequestBody Autovermieter vermieter) {
        vermieter.setId(nextVermieterId++);
        vermieterList.add(vermieter);
        return vermieter;
    }

    // Login (mock)
    @PostMapping("/login")
    public String login(@RequestBody Autovermieter loginRequest) {
        Optional<Autovermieter> user = vermieterList.stream()
                .filter(v -> v.getEmail().equals(loginRequest.getEmail())
                        && v.getPassword().equals(loginRequest.getPassword()))
                .findFirst();
        return user.isPresent() ? "Login erfolgreich!" : "Email oder Passwort falsch!";
    }

    // Auto hinzufügen
    @PostMapping("/{vermieterId}/addCar")
    public Car addCar(@PathVariable Long vermieterId, @RequestBody Car newCar) {
        Optional<Autovermieter> owner = vermieterList.stream()
                .filter(v -> v.getId().equals(vermieterId))
                .findFirst();

        if (owner.isPresent()) {
            newCar.setRented(false);
            newCar.setId(nextCarId++);
            newCar.setOwner(owner.get());
            cars.add(newCar);
            return newCar;
        } else {
            throw new RuntimeException("Vermieter nicht gefunden!");
        }
    }

    // alle Autos eines Vermieters
    @GetMapping("/{vermieterId}/cars")
    public List<Car> getCars(@PathVariable Long vermieterId) {
        return cars.stream()
                .filter(car -> car.getOwner().getId().equals(vermieterId))
                .toList();
    }
}
