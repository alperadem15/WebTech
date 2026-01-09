package com.autovermietung.user;

import jakarta.persistence.*;

@Entity
@Table(name = "kunden")
public class Kunde extends User {

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;

    public Kunde() {}

    public Kunde(String email, String password, String vorname, String nachname) {
        super(email, password);
        this.vorname = vorname;
        this.nachname = nachname;
    }

    // Getter & Setter
    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
}
