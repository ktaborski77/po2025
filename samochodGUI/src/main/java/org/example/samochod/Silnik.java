
package org.example.samochod;

public class Silnik extends Komponent {
    private int maxObroty;
    private int obroty;

    public Silnik(String producent, String model, double waga, double cena, int maxObroty, int obroty) {
        super(producent, model, waga, cena);
        this.maxObroty = maxObroty;
        this.obroty = obroty;
    }

    public int getObroty() {
        return obroty;
    }

    public void uruchom() {
        obroty = 800; // Obroty jałowe
    }

    public void zatrzymaj() {
        obroty = 0;
    }

    public void zwiekszObroty() {
        if (obroty + 500 <= maxObroty) obroty += 500;
        else obroty = maxObroty;
    }

    public void zmniejszObroty() {
        // Zwalnianie/Hamowanie
        if (obroty - 500 >= 800) obroty -= 500;
        else if (obroty > 0) obroty = 800; // Pozycja jałowa
    }
}