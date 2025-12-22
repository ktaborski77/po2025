package org.example.samochodgui;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.example.samochod.*;

import java.util.HashMap;
import java.util.Map;

public class HelloController {

    private Samochod mojSamochod;
    private Map<String, Samochod> garaz = new HashMap<>();
    private AnimationTimer drivingTimer;

    // --- GUI: Kontrolki powiązane z FXML ---
    @FXML private ComboBox<String> carComboBox;
    @FXML private ImageView carImageView;

    @FXML private TextField modelTextField;
    @FXML private TextField plateTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField;

    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearboxPriceTextField;
    @FXML private TextField gearboxWeightTextField;
    @FXML private TextField gearboxGearTextField;

    @FXML private TextField engineNameTextField;
    @FXML private TextField enginePriceTextField;
    @FXML private TextField engineWeightTextField;
    @FXML private TextField engineRpmTextField;

    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchPriceTextField;
    @FXML private TextField clutchWeightTextField;
    @FXML private TextField clutchStateTextField;

    @FXML
    public void initialize() {
        // 1. Inicjalizacja przykładowych samochodów
        Silnik s1 = new Silnik("Ferrari", "V8", 200, 50000, 8000, 0);
        Sprzeglo sp1 = new Sprzeglo("Bosch", "Sport", 10, 2000, false);
        SkrzyniaBiegow sk1 = new SkrzyniaBiegow("ZF", "Manual 6", 50, 8000, 0, 6, sp1);
        Samochod auto1 = new Samochod("Ferrari 488", "K1 FERRARI", 1400, s1, sk1, new Pozycja(20, 50));

        Silnik s2 = new Silnik("Fiat", "1.1", 100, 2000, 5000, 0);
        Sprzeglo sp2 = new Sprzeglo("Valeo", "Standard", 8, 500, false);
        SkrzyniaBiegow sk2 = new SkrzyniaBiegow("Polmo", "Manual 4", 40, 1000, 0, 4, sp2);
        Samochod auto2 = new Samochod("Fiat 126p", "K0 MALUCH", 600, s2, sk2, new Pozycja(20, 50));

        garaz.put(auto1.getModel(), auto1);
        garaz.put(auto2.getModel(), auto2);

        // 2. Wypełnienie ComboBoxa
        carComboBox.getItems().addAll(garaz.keySet());

        // 3. Konfiguracja timera do płynnej jazdy
        drivingTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (mojSamochod != null) {
                    mojSamochod.jedz(); // Wywołuje logikę ruchu z Samochod.java
                    carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
                    refresh(); // Aktualizuje licznik prędkości na bieżąco
                }
            }
        };
    }

    // --- OBSŁUGA ZDARZEŃ (Event Handlers) ---

    @FXML
    protected void onCarSelection(ActionEvent event) {
        String wybranyModel = carComboBox.getValue();
        if (wybranyModel != null) {
            zmienSamochod(wybranyModel);
        }
    }

    private void zmienSamochod(String model) {
        mojSamochod = garaz.get(model);
        // Resetujemy obrazek na mapie
        carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
        refresh();
    }

    @FXML
    protected void onStartButton() {
        if (mojSamochod != null) {
            mojSamochod.wlacz();
            refresh();
        }
    }

    @FXML
    protected void onStopButton() {
        if (mojSamochod != null) {
            mojSamochod.wylacz();
            drivingTimer.stop();
            refresh();
        }
    }

    @FXML
    protected void onDriveButton() {
        if (mojSamochod != null) {
            drivingTimer.start(); // Uruchamia pętlę AnimationTimer
        }
    }

    @FXML
    protected void onIncreaseGearButton() {
        if (mojSamochod != null) {
            mojSamochod.getSkrzynia().zwiekszBieg();
            refresh();
        }
    }

    @FXML
    protected void onDecreaseGearButton() {
        if (mojSamochod != null) {
            mojSamochod.getSkrzynia().zmniejszBieg();
            refresh();
        }
    }

    @FXML
    protected void onIncreaseRpmButton() {
        if (mojSamochod != null) {
            mojSamochod.getSilnik().zwiekszObroty();
            refresh();
        }
    }

    @FXML
    protected void onDecreaseRpmButton() {
        if (mojSamochod != null) {
            mojSamochod.getSilnik().zmniejszObroty();
            refresh();
        }
    }

    @FXML
    protected void onPressClutchButton() {
        if (mojSamochod != null) {
            mojSamochod.getSkrzynia().getSprzeglo().wcisnij();
            refresh();
        }
    }

    @FXML
    protected void onReleaseClutchButton() {
        if (mojSamochod != null) {
            mojSamochod.getSkrzynia().getSprzeglo().zwolnij();
            refresh();
        }
    }

    // --- METODA ODŚWIEŻAJĄCA GUI ---
    private void refresh() {
        if (mojSamochod == null) return;

        // Dane ogólne
        modelTextField.setText(mojSamochod.getModel());
        plateTextField.setText(mojSamochod.getNrRejestracyjny());
        weightTextField.setText(String.valueOf(mojSamochod.getWaga()));
        speedTextField.setText(String.format("%.2f", mojSamochod.getPredkosc()));

        // Silnik
        Silnik s = mojSamochod.getSilnik();
        engineNameTextField.setText(s.getProducent() + " " + s.getModel());
        engineRpmTextField.setText(String.valueOf(s.getObroty()));
        engineWeightTextField.setText(String.valueOf(s.getWaga()));
        enginePriceTextField.setText(String.valueOf(s.getCena()));

        // Skrzynia
        SkrzyniaBiegow sk = mojSamochod.getSkrzynia();
        gearboxNameTextField.setText(sk.getProducent() + " " + sk.getModel());
        gearboxGearTextField.setText(String.valueOf(sk.getAktualnyBieg()));
        gearboxWeightTextField.setText(String.valueOf(sk.getWaga()));
        gearboxPriceTextField.setText(String.valueOf(sk.getCena()));

        // Sprzęgło
        Sprzeglo sp = sk.getSprzeglo();
        clutchNameTextField.setText(sp.getProducent() + " " + sp.getModel());
        clutchStateTextField.setText(sp.isStanSprzegla() ? "Wciśnięte" : "Zwolnione");
        clutchWeightTextField.setText(String.valueOf(sp.getWaga()));
        clutchPriceTextField.setText(String.valueOf(sp.getCena()));
    }
}