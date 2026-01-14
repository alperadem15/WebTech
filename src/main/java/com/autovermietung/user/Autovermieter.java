package com.autovermietung.user;

import com.autovermietung.Car;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autovermieter")
public class Autovermieter extends User {

    private String firmenname;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Car> cars;

    public Autovermieter() {}

    public Autovermieter(Long id, String email, String password, String firmenname) {
        super(email, password);
        this.id = id;
        this.firmenname = firmenname;
    }

    public String getFirmenname() { return firmenname; }
    public void setFirmenname(String firmenname) { this.firmenname = firmenname; }

    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }
}
