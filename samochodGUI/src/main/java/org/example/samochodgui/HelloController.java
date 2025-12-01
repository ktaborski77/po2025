package org.example.samochodgui;

import javafx.animation.AnimationTimer; // NOWY IMPORT
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.samochod.*;

import java.util.HashMap;
import java.util.Map;

public class HelloController {

    private Samochod mojSamochod;
    private Map<String, Samochod> garaz = new HashMap<>();

    // NOWY ELEMENT: Timer do ciągłego ruchu
    private AnimationTimer drivingTimer;

    // --- GUI: Wybór auta i Mapa ---
    @FXML private ComboBox<String> carComboBox;
    @FXML private ImageView carImageView;

    // --- GUI: Pola Samochodu ---
    @FXML private TextField modelTextField;
    @FXML private TextField plateTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField;

    // --- GUI: Pola Komponentów ---
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

    // --- GUI: Przyciski
    @FXML private Button driveButton;


    @FXML
    public void initialize() {
        stworzKomponenty();

        carComboBox.getItems().addAll(garaz.keySet());
        carComboBox.getSelectionModel().selectFirst();
        zmienSamochod(carComboBox.getSelectionModel().getSelectedItem());

        // Ładowanie grafiki
        try {
            Image carIcon = new Image(getClass().getResourceAsStream("car-icon.jpg"));
            carImageView.setImage(carIcon);
        } catch (Exception e) {
            System.err.println("Brak pliku car-icon.jpg w zasobach.");
        }

        // --- INICJALIZACJA TIMER'A JAZDY ---
        drivingTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (mojSamochod != null) {
                    // Ta metoda wywołuje się co klatkę (ok. 60 razy na sekundę)
                    mojSamochod.jedz();
                    refresh();
                }
            }
        };
    }

    private void stworzKomponenty() {
        // Upewnienie się, że konstruktory pasują do Komponent(producent, model, waga, cena)
        Sprzeglo sprzegloFiat = new Sprzeglo("Sachs", "Standard", 5.0, 300.0, false);
        SkrzyniaBiegow skrzyniaFiat = new SkrzyniaBiegow("Fiat", "Manual 4b", 30.0, 1000.0, 0, 4, sprzegloFiat);
        Silnik silnikFiat = new Silnik("FSM", "0.65L", 80.0, 1500.0, 5000, 0);
        Pozycja pozycjaFiat = new Pozycja(20, 50);
        Samochod fiat = new Samochod("Fiat 126p", "KRA 1234", 600.0, silnikFiat, skrzyniaFiat, pozycjaFiat);

        Sprzeglo sprzegloFerrari = new Sprzeglo("Brembo", "Ceramiczne", 8.0, 5000.0, false);
        SkrzyniaBiegow skrzyniaFerrari = new SkrzyniaBiegow("Ferrari", "F1 DCT", 60.0, 40000.0, 0, 8, sprzegloFerrari);
        Silnik silnikFerrari = new Silnik("Ferrari", "V8 Biturbo", 200.0, 150000.0, 9000, 0);
        Pozycja pozycjaFerrari = new Pozycja(20, 50);
        Samochod ferrari = new Samochod("Ferrari 488", "W0 SPEED", 1400.0, silnikFerrari, skrzyniaFerrari, pozycjaFerrari);

        garaz.put(fiat.getModel(), fiat);
        garaz.put(ferrari.getModel(), ferrari);
    }

    @FXML
    public void onCarSelection(ActionEvent event) {
        String wybranyModel = carComboBox.getSelectionModel().getSelectedItem();
        zmienSamochod(wybranyModel);
    }

    private void zmienSamochod(String nazwaModelu) {
        if (nazwaModelu != null && garaz.containsKey(nazwaModelu)) {
            // Zatrzymujemy timer przy zmianie samochodu, aby nie działał w tle
            if (drivingTimer != null) drivingTimer.stop();

            mojSamochod = garaz.get(nazwaModelu);
            refresh();

            carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
            carImageView.setLayoutY(mojSamochod.getAktualnaPozycja().getY());
        }
    }

    // --- AKCJE PRZYCISKÓW ---

    @FXML
    protected void onDriveButton() {
        if (mojSamochod != null) {
            drivingTimer.start(); // START PĘTLI JAZDY!
        }
    }

    // Używamy onStopButton do wyłączenia silnika i zatrzymania ciągłej jazdy
    @FXML
    protected void onStopButton() {
        if (mojSamochod != null) {
            if (drivingTimer != null) drivingTimer.stop(); // ZATRZYMUJEMY PĘTLĘ
            mojSamochod.wylacz();
            refresh();
        }
    }

    @FXML protected void onStartButton() { mojSamochod.wlacz(); refresh(); }
    @FXML protected void onIncreaseGearButton() { mojSamochod.getSkrzynia().zwiekszBieg(); refresh(); }
    @FXML protected void onDecreaseGearButton() { mojSamochod.getSkrzynia().zmniejszBieg(); refresh(); }
    @FXML protected void onIncreaseRpmButton() { mojSamochod.getSilnik().zwiekszObroty(); refresh(); }
    @FXML protected void onDecreaseRpmButton() { mojSamochod.getSilnik().zmniejszObroty(); refresh(); }
    @FXML protected void onPressClutchButton() { mojSamochod.getSkrzynia().getSprzeglo().wcisnij(); refresh(); }
    @FXML protected void onReleaseClutchButton() { mojSamochod.getSkrzynia().getSprzeglo().zwolnij(); refresh(); }


    // --- ODŚWIEŻANIE WIDOKU ---
    private void refresh() {
        if (mojSamochod == null) return;

        // ... (Logika odświeżania pól tekstowych - jest taka sama jak wcześniej) ...
        modelTextField.setText(mojSamochod.getModel());
        plateTextField.setText(mojSamochod.getNrRejestracyjny());
        weightTextField.setText(String.valueOf(mojSamochod.getWaga()));
        speedTextField.setText(String.format("%.2f", mojSamochod.getPredkosc()));

        Silnik s = mojSamochod.getSilnik();
        engineNameTextField.setText(s.getProducent() + " " + s.getModel());
        enginePriceTextField.setText(String.valueOf(s.getCena()));
        engineWeightTextField.setText(String.valueOf(s.getWaga()));
        engineRpmTextField.setText(String.valueOf(s.getObroty()));

        SkrzyniaBiegow sb = mojSamochod.getSkrzynia();
        gearboxNameTextField.setText(sb.getProducent() + " " + sb.getModel());
        gearboxPriceTextField.setText(String.valueOf(sb.getCena()));
        gearboxWeightTextField.setText(String.valueOf(sb.getWaga()));
        gearboxGearTextField.setText(String.valueOf(sb.getAktualnyBieg()));

        Sprzeglo sp = sb.getSprzeglo();
        clutchNameTextField.setText(sp.getProducent() + " " + sp.getModel());
        clutchPriceTextField.setText(String.valueOf(sp.getCena()));
        clutchWeightTextField.setText(String.valueOf(sp.getWaga()));
        clutchStateTextField.setText(sp.isStanSprzegla() ? "Wciśnięte" : "Zwolnione");

        // Aktualizacja pozycji auta na mapie
        carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
        carImageView.setLayoutY(mojSamochod.getAktualnaPozycja().getY());
    }
}