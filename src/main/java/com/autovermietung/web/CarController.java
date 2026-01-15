package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UmsatzRepository umsatzRepository;

    // oben in der Klasse (z.B. nach @Autowired)
    public record CarCustomerDto(
            Long id,
            String brand,
            String model,
            double pricePerDay,
            boolean rented,
            Long ownerId,
            String ownerFirmenname
    ) {}

    @GetMapping
    public List<CarCustomerDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> new CarCustomerDto(
                        car.getId(),
                        car.getBrand(),
                        car.getModel(),
                        car.getPricePerDay(),
                        car.isRented(),
                        car.getOwner() != null ? car.getOwner().getId() : null,
                        car.getOwner() != null ? car.getOwner().getFirmenname() : null
                ))
                .toList();
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

    // Neues Auto hinzufügen (Owner wird woanders gesetzt)
    @PostMapping
    public Car addCar(@RequestBody Car car) {
        car.setRented(false);
        return carRepository.save(car);
    }

    // ❗ Auto mieten (Kunde)
    @PostMapping("/rent/{id}")
    public String rentCar(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> {
                    if (car.isRented()) {
                        return "Dieses Auto ist bereits vermietet!";
                    }

                    // Auto muss einen Vermieter haben, sonst kein Umsatz zuordenbar
                    if (car.getOwner() == null) {
                        return "Fehler: Auto hat keinen Vermieter (Owner)!";
                    }

                    car.setRented(true);
                    carRepository.save(car);

                    // Umsatz-Event speichern (1 Event pro Miete)
                    String carName = car.getBrand() + " " + car.getModel();
                    double amount = car.getPricePerDay();

                    umsatzRepository.save(new Umsatz(car.getOwner(), carName, amount));

                    return "Auto wurde erfolgreich gemietet!";
                })
                .orElse("Auto nicht gefunden!");
    }


    // Auto löschen (nur Owner)
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id, @RequestParam Long ownerId) {
        Car car = carRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Auto nicht gefunden oder nicht dein Auto!"));
        carRepository.delete(car);
    }

    // Mietstatus setzen (nur Owner)
    @PatchMapping("/{id}/rented")
    public Car setRented(@PathVariable Long id,
                         @RequestParam boolean rented,
                         @RequestParam Long ownerId) {

        Car car = carRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Auto nicht gefunden oder nicht dein Auto!"));

        car.setRented(rented);
        return carRepository.save(car);
    }
}
