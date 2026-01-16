package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.CarRepository;
import com.autovermietung.Umsatz;
import com.autovermietung.UmsatzRepository;
import com.autovermietung.user.Autovermieter;
import com.autovermietung.user.AutovermieterRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vermieter")
public class AutovermieterController {

    @Autowired
    private AutovermieterRepository vermieterRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UmsatzRepository umsatzRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Autovermieter register(@RequestBody Autovermieter vermieter) {
        vermieter.setPassword(passwordEncoder.encode(vermieter.getPassword()));
        return vermieterRepository.save(vermieter);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody Autovermieter loginRequest) {
        Autovermieter v = vermieterRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email oder Passwort falsch!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), v.getPassword())) {
            throw new RuntimeException("Email oder Passwort falsch!");
        }

        return new LoginResponse(v.getId(), "Login erfolgreich!");
    }

    public record LoginResponse(Long vermieterId, String message) {}

    @PostMapping("/{vermieterId}/addCar")
    public Car addCar(@PathVariable Long vermieterId, @RequestBody Car newCar) {
        Autovermieter owner = vermieterRepository.findById(vermieterId)
                .orElseThrow(() -> new RuntimeException("Vermieter nicht gefunden!"));

        newCar.setOwner(owner);
        newCar.setRented(false);

        return carRepository.save(newCar);
    }

    @GetMapping("/{vermieterId}/cars")
    public List<Car> getCars(@PathVariable Long vermieterId) {
        return carRepository.findByOwnerId(vermieterId);
    }

    // --- Umsatz ---
    public record UmsatzEvent(String carName, double amount, String date) {}
    public record UmsatzResponse(double gesamtUmsatz, List<UmsatzEvent> events) {}

    @GetMapping("/{vermieterId}/umsatz")
    public UmsatzResponse getUmsatz(@PathVariable Long vermieterId) {

        vermieterRepository.findById(vermieterId)
                .orElseThrow(() -> new RuntimeException("Vermieter nicht gefunden!"));

        var rows = umsatzRepository.findByVermieterIdOrderByCreatedAtDesc(vermieterId);

        var events = rows.stream()
                .map(r -> new UmsatzEvent(
                        r.getCarName(),
                        r.getAmount(),
                        r.getCreatedAt().toLocalDate().toString()
                ))
                .toList();

        double gesamt = rows.stream().mapToDouble(r -> r.getAmount()).sum();

        return new UmsatzResponse(gesamt, events);
    }

}
