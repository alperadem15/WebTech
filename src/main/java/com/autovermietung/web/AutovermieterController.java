package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.user.Autovermieter;
import com.autovermietung.user.AutovermieterRepository;
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

    @PostMapping("/register")
    public Autovermieter register(@RequestBody Autovermieter vermieter) {
        return vermieterRepository.save(vermieter);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody Autovermieter loginRequest) {
        Autovermieter v = vermieterRepository.findByEmail(loginRequest.getEmail())
                .filter(x -> x.getPassword().equals(loginRequest.getPassword()))
                .orElseThrow(() -> new RuntimeException("Email oder Passwort falsch!"));

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
}
