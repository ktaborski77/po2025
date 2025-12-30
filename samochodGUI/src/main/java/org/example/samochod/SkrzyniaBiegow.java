
package org.example.samochod;

public class SkrzyniaBiegow extends Komponent {
    int aktualnyBieg;
    int iloscBiegow;
    Sprzeglo sprzeglo;

    // Zakładam, że konstruktor przyjmuje wszystkie potrzebne argumenty (waga, cena, itd. z Komponent)
    public SkrzyniaBiegow(String producent, String model, double waga, double cena, int aktualnyBieg, int iloscBiegow, Sprzeglo sprzeglo) {
        super(producent, model, waga, cena); // Użycie ulepszonego konstruktora Komponentu
        this.aktualnyBieg = aktualnyBieg;
        this.iloscBiegow = iloscBiegow;
        this.sprzeglo = sprzeglo;
    }

    // Konieczne, aby poprawić błąd kompilacji
    public void setAktualnyBieg(int nowyBieg) {
        this.aktualnyBieg = nowyBieg;
    }

    public void zwiekszBieg() {
        if (sprzeglo.isStanSprzegla()) {
            if (aktualnyBieg < iloscBiegow) {
                aktualnyBieg++;
            }
        } else {
            System.out.println("Wciśnij sprzęgło, aby zmienić bieg!");
        }
    }

    public void zmniejszBieg() {
        if (sprzeglo.isStanSprzegla()) {
            if (aktualnyBieg > 0) {
                aktualnyBieg--;
            }
        } else {
            System.out.println("Wciśnij sprzęgło, aby zmienić bieg!");
        }
    }

    public int getAktualnyBieg() { return aktualnyBieg; }
    public Sprzeglo getSprzeglo() { return sprzeglo; }
}