package com.autovermietung.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KundeRepository extends JpaRepository<Kunde, Long> {
    Optional<Kunde> findByEmail(String email);
}
