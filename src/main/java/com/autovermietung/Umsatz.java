package com.autovermietung;

import com.autovermietung.user.Autovermieter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "umsatz")
public class Umsatz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Für Anzeige (z.B. "Mercedes Benz C63 AMG")
    @Column(nullable = false)
    private String carName;

    // Betrag (z.B. pricePerDay)
    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vermieter_id")
    private Autovermieter vermieter;

    public Umsatz() {}

    public Umsatz(Autovermieter vermieter, String carName, double amount) {
        this.vermieter = vermieter;
        this.carName = carName;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCarName() { return carName; }
    public double getAmount() { return amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Autovermieter getVermieter() { return vermieter; }

    public void setCarName(String carName) { this.carName = carName; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setVermieter(Autovermieter vermieter) { this.vermieter = vermieter; }
}
