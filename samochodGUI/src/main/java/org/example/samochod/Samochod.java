package org.example.samochod;

public class Samochod {
    private String model;
    private String nrRejestracyjny;
    private double waga;
    private double predkosc; // Aktualna prędkość

    private Silnik silnik;
    private SkrzyniaBiegow skrzynia;
    private Pozycja aktualnaPozycja;

    public Samochod(String model, String nrRejestracyjny, double waga, Silnik silnik, SkrzyniaBiegow skrzynia, Pozycja aktualnaPozycja) {
        this.model = model;
        this.nrRejestracyjny = nrRejestracyjny;
        this.waga = waga;
        this.silnik = silnik;
        this.skrzynia = skrzynia;
        this.aktualnaPozycja = aktualnaPozycja;
        this.predkosc = 0.0;
    }

    public void wlacz() {
        silnik.uruchom();
    }

    public void wylacz() {
        silnik.zatrzymaj();
        this.predkosc = 0.0;
        // Używamy setter'a naprawionego w poprzednim kroku
        if (skrzynia != null) {
            skrzynia.setAktualnyBieg(0);
        }
    }

    // --- KLUCZOWA METODA DLA RUCHU WYWOŁYWANA CO CHWILĘ ---
    public void jedz() {
        // Obliczenie, czy mamy napęd
        boolean maNaped = silnik.getObroty() > 0
                && !skrzynia.getSprzeglo().isStanSprzegla()
                && skrzynia.getAktualnyBieg() > 0;

        if (maNaped) {
            // Prędkość rośnie, ale wolniej, bo jedz() jest wywoływane co chwilę
            double przyspieszenie = (double) (silnik.getObroty() * skrzynia.getAktualnyBieg()) / 50000.0;
            this.predkosc += przyspieszenie;

            if (this.predkosc > 100.0) this.predkosc = 100.0; // Ograniczenie prędkości

        } else {
            // Hamowanie/toczenie
            if (predkosc > 0) predkosc -= 0.5;
            if (predkosc < 0) predkosc = 0;
        }

        // Aktualizacja pozycji X na podstawie aktualnej prędkości
        // Jeśli prędkość to 100, przesuwamy o 100*0.1 = 10px na klatkę
        aktualnaPozycja.aktualizujPozycje(this.predkosc * 0.1, 0);
    }

    // Gettery
    public Silnik getSilnik() { return silnik; }
    public SkrzyniaBiegow getSkrzynia() { return skrzynia; }
    public Pozycja getAktualnaPozycja() { return aktualnaPozycja; }
    public String getModel() { return model; }
    public String getNrRejestracyjny() { return nrRejestracyjny; }
    public double getWaga() { return waga; }
    public double getPredkosc() { return predkosc; }
}