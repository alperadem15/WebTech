package com.autovermietung.web;

import com.autovermietung.Car;
import com.autovermietung.CarRepository;
import com.autovermietung.Umsatz;
import com.autovermietung.UmsatzRepository;
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

    // Kunden DTO (mit Firmenname)
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
        car.setRentedByKundeId(null);
        return carRepository.save(car);
    }

    // ✅ Auto mieten (Kunde) -> jetzt mit kundeId
    @PostMapping("/rent/{id}")
    public String rentCar(@PathVariable Long id, @RequestParam Long kundeId) {
        return carRepository.findById(id)
                .map(car -> {
                    if (car.isRented()) return "Dieses Auto ist bereits vermietet!";
                    if (car.getOwner() == null) return "Fehler: Auto hat keinen Vermieter (Owner)!";

                    car.setRented(true);
                    car.setRentedByKundeId(kundeId);
                    carRepository.save(car);

                    // ✅ Umsatz-Event speichern
                    String carName = car.getBrand() + " " + car.getModel();
                    double amount = car.getPricePerDay();
                    umsatzRepository.save(new Umsatz(car.getOwner(), carName, amount));

                    return "Auto wurde erfolgreich gemietet!";
                })
                .orElse("Auto nicht gefunden!");
    }

    // ✅ "Meine Miete" -> NIE 500, sondern 200 + null wenn keine Miete
    @GetMapping("/my-rental")
    public CarCustomerDto getMyRental(@RequestParam Long kundeId) {
        return carRepository.findFirstByRentedByKundeIdAndRentedTrue(kundeId)
                .map(car -> new CarCustomerDto(
                        car.getId(),
                        car.getBrand(),
                        car.getModel(),
                        car.getPricePerDay(),
                        car.isRented(),
                        car.getOwner() != null ? car.getOwner().getId() : null,
                        car.getOwner() != null ? car.getOwner().getFirmenname() : null
                ))
                .orElse(null);
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

        // wenn Owner manuell auf "nicht vermietet" setzt -> Zuordnung löschen
        if (!rented) {
            car.setRentedByKundeId(null);
        }

        return carRepository.save(car);
    }
}
