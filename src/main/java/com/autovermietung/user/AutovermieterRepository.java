package com.autovermietung.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutovermieterRepository extends JpaRepository<Autovermieter, Long> {
    Optional<Autovermieter> findByEmail(String email);
}
