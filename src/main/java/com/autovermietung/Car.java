package com.autovermietung;

import com.autovermietung.user.Autovermieter;

public class Car {

    private Long id;
    private String brand;
    private String model;
    private double pricePerDay;
    private Autovermieter owner;
    private boolean rented; // NEU: zeigt, ob Auto gemietet ist

    // Konstruktor
    public Car(Long id, String brand, String model, double pricePerDay, Autovermieter owner) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.owner = owner;
        this.rented = false; // standardmäßig verfügbar
    }

    // Getter
    public Long getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getPricePerDay() { return pricePerDay; }
    public Autovermieter getOwner() { return owner; }
    public boolean isRented() { return rented; }

    // Setter
    public void setId(Long id) { this.id = id; }
    public void setOwner(Autovermieter owner) { this.owner = owner; }
    public void setRented(boolean rented) { this.rented = rented; }

    // Optional: ToString für Debug
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", owner=" + owner.getFirmenname() +
                ", rented=" + rented +
                '}';
    }
}
