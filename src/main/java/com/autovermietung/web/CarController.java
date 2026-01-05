package com.autovermietung.web;

import com.autovermietung.Car;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "https://frontendwebtech-oq0k.onrender.com")
@RestController
public class CarController {

    @GetMapping("/cars")
    public List<Car> getCars() {
        return List.of(
                new Car(1L, "VW", "Tiguan", 99.99),
                new Car(2L, "Mercedes-Benz", "S500", 350.00),
                new Car(3L, "Tesla", "Model s", 150.00)
        );
    }
}
