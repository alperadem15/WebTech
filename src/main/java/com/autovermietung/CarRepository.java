package com.autovermietung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwnerId(Long ownerId);
    List<Car> findByRentedFalse();

    Optional<Car> findByIdAndOwnerId(Long id, Long ownerId);

    // "Meine Miete"
    Optional<Car> findFirstByRentedByKundeIdAndRentedTrue(Long rentedByKundeId);
}
