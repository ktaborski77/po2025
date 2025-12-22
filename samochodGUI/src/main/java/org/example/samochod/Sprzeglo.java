
package org.example.samochod;

public class Sprzeglo extends Komponent {
    private boolean stanSprzegla;

    public Sprzeglo(String producent, String model, double waga, double cena, boolean stanSprzegla) {
        super(producent, model, waga, cena);
        this.stanSprzegla = stanSprzegla;
    }

    public void wcisnij() {
        stanSprzegla = true;
    }

    public void zwolnij() {
        stanSprzegla = false;
    }

    public boolean isStanSprzegla() {
        return stanSprzegla;
    }
}
