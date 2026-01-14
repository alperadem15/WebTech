package com.autovermietung.web;

import com.autovermietung.user.Kunde;
import com.autovermietung.user.KundeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kunde")
public class KundeController {

    @Autowired
    private KundeRepository kundeRepository;

    // Registrierung
    @PostMapping("/register")
    public Kunde register(@RequestBody Kunde kunde) {
        // prüfen, ob Email schon existiert
        Optional<Kunde> existing = kundeRepository.findByEmail(kunde.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email bereits registriert!");
        }
        return kundeRepository.save(kunde);
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestBody Kunde loginRequest) {
        Optional<Kunde> kunde = kundeRepository.findByEmail(loginRequest.getEmail());
        if (kunde.isPresent() && kunde.get().getPassword().equals(loginRequest.getPassword())) {
            return "Login erfolgreich!";
        }
        return "Email oder Passwort falsch!";
    }

    // Alle Kunden (optional)
    @GetMapping("/all")
    public List<Kunde> getAllKunden() {
        return kundeRepository.findAll();
    }
}
