package com.autovermietung;

import com.autovermietung.user.Autovermieter;
import jakarta.persistence.*;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private double pricePerDay;
    private boolean rented = false;

    @ManyToOne
    @JoinColumn(name = "owner_id") // FK zu Autovermieter
    private Autovermieter owner;

    public Car() {} // JPA benötigt Default-Konstruktor

    public Car(String brand, String model, double pricePerDay, Autovermieter owner) {
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.owner = owner;
        this.rented = false;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

    public boolean isRented() { return rented; }
    public void setRented(boolean rented) { this.rented = rented; }

    public Autovermieter getOwner() { return owner; }
    public void setOwner(Autovermieter owner) { this.owner = owner; }
}
