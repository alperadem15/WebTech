package com.autovermietung.web;

import com.autovermietung.user.Kunde;
import com.autovermietung.user.KundeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Kunde register(@RequestBody Kunde kunde) {
        Optional<Kunde> existing = kundeRepository.findByEmail(kunde.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email bereits registriert!");
        }

        kunde.setPassword(passwordEncoder.encode(kunde.getPassword()));
        return kundeRepository.save(kunde);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody Kunde loginRequest) {
        Kunde k = kundeRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email oder Passwort falsch!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), k.getPassword())) {
            throw new RuntimeException("Email oder Passwort falsch!");
        }

        return new LoginResponse(k.getId(), "Login erfolgreich!");
    }

    public record LoginResponse(Long kundeId, String message) {}

    // Alle Kunden (optional)
    @GetMapping("/all")
    public List<Kunde> getAllKunden() {
        return kundeRepository.findAll();
    }
}
