package org.example.samochod;

import java.util.ArrayList;
import java.util.List;

public class Samochod extends Thread {
    private String model;
    private String nrRejestracyjny;
    private double waga;
    private double predkosc;

    private Silnik silnik;
    private SkrzyniaBiegow skrzynia;
    private Pozycja aktualnaPozycja;

    // Pola dla ruchu autonomicznego (myszka)
    private Pozycja cel;
    private boolean dziala = true;

    // Lista obserwatorów
    private List<Listener> listeners = new ArrayList<>();

    public Samochod(String model, String nrRejestracyjny, double waga, Silnik silnik, SkrzyniaBiegow skrzynia, Pozycja aktualnaPozycja) {
        this.model = model;
        this.nrRejestracyjny = nrRejestracyjny;
        this.waga = waga;
        this.silnik = silnik;
        this.skrzynia = skrzynia;
        this.aktualnaPozycja = aktualnaPozycja;
        this.predkosc = 0.0;

        // Uruchamiamy wątek
        this.start();
    }

    // --- POPRAWKA 1: Wyświetlanie poprawnej nazwy w ComboBox ---
    @Override
    public String toString() {
        return model + " (" + nrRejestracyjny + ")";
    }

    // Metody wzorca Obserwator
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    public void jedzDo(Pozycja cel) {
        this.cel = cel;
    }

    public void zakonczWatek() {
        this.dziala = false;
    }

    public void wlacz() {
        silnik.uruchom();
        notifyListeners();
    }

    public void wylacz() {
        silnik.zatrzymaj();
        this.predkosc = 0.0;
        if (skrzynia != null) {
            skrzynia.setAktualnyBieg(0);
        }
        notifyListeners();
    }

    // Fizyka obliczająca prędkość na podstawie silnika i biegów
    public void obliczFizyke() {
        boolean maNaped = silnik.getObroty() > 0
                && !skrzynia.getSprzeglo().isStanSprzegla()
                && skrzynia.getAktualnyBieg() > 0;

        if (maNaped) {
            // Prędkość rośnie w zależności od obrotów i biegu
            double przyspieszenie = (double) (silnik.getObroty() * skrzynia.getAktualnyBieg()) / 40000.0;

            if (this.predkosc < 100.0) {
                this.predkosc += przyspieszenie * 0.1; // Delta time
            }
            if (this.predkosc > 100.0) this.predkosc = 100.0;

        } else {
            // Hamowanie silnikiem / toczenie się
            if (predkosc > 0) predkosc -= 0.5;
            if (predkosc < 0) predkosc = 0;
        }
    }

    @Override
    public void run() {
        double deltat = 0.1;

        while (dziala) {
            try {
                // 1. Obliczamy fizykę (zmiana prędkości w zależności od gazu/biegu)
                obliczFizyke();

                // 2. Obsługa ruchu
                if (cel != null) {
                    // Mamy cel (kliknięcie myszką) - jedziemy w tamtą stronę
                    double dx = cel.getX() - aktualnaPozycja.getX();
                    double dy = cel.getY() - aktualnaPozycja.getY();
                    double odleglosc = Math.sqrt(dx * dx + dy * dy);

                    // --- POPRAWKA 2: Zatrzymanie po dojechaniu do celu ---
                    if (odleglosc > 1.0) {
                        // Jeszcze jedziemy
                        double moveX = (dx / odleglosc) * getPredkosc() * deltat;
                        double moveY = (dy / odleglosc) * getPredkosc() * deltat;
                        aktualnaPozycja.aktualizujPozycje(moveX, moveY);
                    } else {
                        // Dotarliśmy do celu!
                        // Ustawiamy pozycję idealnie w punkcie celu
                        aktualnaPozycja.aktualizujPozycje(dx, dy);

                        // Zerujemy cel
                        cel = null;

                        // WAŻNE: Zerujemy prędkość i wrzucamy luz, żeby auto nie ruszyło samo
                        this.predkosc = 0.0;
                        if (skrzynia != null) {
                            skrzynia.setAktualnyBieg(0);
                        }
                    }
                } else {
                    // Brak celu (tryb manualny) - jedziemy tylko prosto (oś X)
                    // Auto ruszy tylko jeśli fizyka wyliczyła prędkość > 0 (czyli dodano gazu i wrzucono bieg)
                    if (this.predkosc > 0) {
                        aktualnaPozycja.aktualizujPozycje(this.predkosc * deltat, 0);
                    }
                }

                // 3. Powiadom GUI
                notifyListeners();

                // 4. Czekamy (ok. 60 FPS)
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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