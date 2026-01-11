package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.user.Autovermieter;
import com.autovermietung.user.AutovermieterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://frontendwebtech-oq0k.onrender.com")
@RequestMapping("/vermieter")
public class AutovermieterController {

    @Autowired
    private AutovermieterRepository vermieterRepository;

    @Autowired
    private CarController carController; // für Autos des Vermieters

    // Registrierung
    @PostMapping("/register")
    public Autovermieter register(@RequestBody Autovermieter vermieter) {
        return vermieterRepository.save(vermieter);
    }

    // Login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody Autovermieter loginRequest) {
        Autovermieter v = vermieterRepository.findByEmail(loginRequest.getEmail())
                .filter(x -> x.getPassword().equals(loginRequest.getPassword()))
                .orElseThrow(() -> new RuntimeException("Email oder Passwort falsch!"));

        return new LoginResponse(v.getId(), "Login erfolgreich!");
    }

    public static class LoginResponse {
        public Long vermieterId;
        public String message;

        public LoginResponse(Long vermieterId, String message) {
            this.vermieterId = vermieterId;
            this.message = message;
        }
    }


    // Auto hinzufügen
    @PostMapping("/{vermieterId}/addCar")
    public Car addCar(@PathVariable Long vermieterId, @RequestBody Car newCar) {
        Autovermieter owner = vermieterRepository.findById(vermieterId)
                .orElseThrow(() -> new RuntimeException("Vermieter nicht gefunden!"));
        newCar.setRented(false);
        newCar.setOwner(owner);
        return carController.addCar(newCar); // CarController speichert in DB
    }

    // alle Autos eines Vermieters
    @GetMapping("/{vermieterId}/cars")
    public List<Car> getCars(@PathVariable Long vermieterId) {
        return carController.getCarsByOwner(vermieterId);
    }
}
