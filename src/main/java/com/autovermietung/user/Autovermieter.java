package com.autovermietung.user;

import com.autovermietung.Car;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autovermieter")
public class Autovermieter extends User {

    private String firmenname;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars; // Alle Autos des Vermieters

    public Autovermieter() {}

    public Autovermieter(String email, String password, String firmenname) {
        super(id, email, password);
        this.firmenname = firmenname;
    }

    // Getter & Setter
    public String getFirmenname() { return firmenname; }
    public void setFirmenname(String firmenname) { this.firmenname = firmenname; }

    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }
}
