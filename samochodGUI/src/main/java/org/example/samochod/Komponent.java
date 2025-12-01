package org.example.samochod;

public class Komponent {
    protected String producent;
    protected String model;
    protected double waga;
    protected double cena;

    public Komponent(String producent, String model, double waga, double cena) {
        this.producent = producent;
        this.model = model;
        this.waga = waga;
        this.cena = cena;
    }

    public String getProducent() { return producent; }
    public String getModel() { return model; }
    public double getWaga() { return waga; }
    public double getCena() { return cena; }
}