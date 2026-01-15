package com.autovermietung;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UmsatzRepository extends JpaRepository<Umsatz, Long> {
    List<Umsatz> findByVermieterIdOrderByCreatedAtDesc(Long vermieterId);
}
