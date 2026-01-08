package com.autovermietung.user;

public class Kunde extends User {

    private String vorname;
    private String nachname;

    public Kunde() {}

    public Kunde(Long id, String email, String password, String vorname, String nachname) {
        super(id, email, password);
        this.vorname = vorname;
        this.nachname = nachname;
    }

    // Getter & Setter
    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
}
