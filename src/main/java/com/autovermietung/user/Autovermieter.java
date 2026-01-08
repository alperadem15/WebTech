package com.autovermietung.user;

public class Autovermieter extends User {

    private String firmenname;

    public Autovermieter() {}

    public Autovermieter(Long id, String email, String password, String firmenname) {
        super(id, email, password);
        this.firmenname = firmenname;
    }

    // Getter & Setter
    public String getFirmenname() { return firmenname; }
    public void setFirmenname(String firmenname) { this.firmenname = firmenname; }
}
