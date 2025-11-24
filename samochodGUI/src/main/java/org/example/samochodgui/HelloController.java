package org.example.samochodgui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import symulator.Pozycja;
import symulator.Samochod;
import symulator.Silnik;
import symulator.SkrzyniaBiegow;

public class HelloController {

    // ================== FXML KONTROLKI (Nazwy muszą pasować do fx:id w FXML!) ==================

    @FXML private ComboBox<String> carComboBox;

    // Sekcja Samochód
    @FXML private TextField modelTextField;
    @FXML private TextField plateTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField;
    @FXML private Button startButton;
    @FXML private Button stopButton;

    // Sekcja Skrzynia
    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearboxGearTextField; // W FXML to jest ID, a nie gearTextField
    @FXML private Button increaseGearButton;
    @FXML private Button decreaseGearButton;

    // Sekcja Silnik
    @FXML private TextField engineNameTextField;
    @FXML private TextField engineRpmTextField; // W FXML to jest ID, a nie rpmTextField
    @FXML private Button increaseRpmButton;
    @FXML private Button decreaseRpmButton;

    // ================== OBIEKTY SYMULATORA ==================

    private Samochod aktualnySamochod;

    // Przykładowa baza danych samochodów
    private final Samochod[] samochody = {
            new Samochod(
                    new Silnik("Audi", "1.8T", 6500),
                    new SkrzyniaBiegow("ZF", "6HP", 6),
                    new Pozycja()
            ),
            new Samochod(
                    new Silnik("BMW", "2.5 R6", 7000),
                    new SkrzyniaBiegow("Getrag", "5MT", 5),
                    new Pozycja()
            )
    };

    // ================== INICJALIZACJA ==================

    @FXML
    public void initialize() {
        // Ładowanie listy do ComboBoxa
        carComboBox.setItems(FXCollections.observableArrayList(
                "Audi 1.8T",
                "BMW 2.5 R6"
        ));

        // Obsługa wyboru
        carComboBox.setOnAction(e -> onCarSelected());

        // Przypisanie akcji przycisków (można to też robić w FXML przez onAction="#...")
        startButton.setOnAction(e -> onStartButton());
        stopButton.setOnAction(e -> onStopButton());
        increaseGearButton.setOnAction(e -> onIncreaseGearButton());
        decreaseGearButton.setOnAction(e -> onDecreaseGearButton());
        increaseRpmButton.setOnAction(e -> onIncreaseRpm());
        decreaseRpmButton.setOnAction(e -> onDecreaseRpm());
    }

    // ================== LOGIKA ==================

    private void onCarSelected() {
        int index = carComboBox.getSelectionModel().getSelectedIndex();
        if (index < 0) return;

        aktualnySamochod = samochody[index];
        updateFields();
    }

    private void updateFields() {
        if (aktualnySamochod == null) return;

        // Aktualizacja pól tekstowych danymi z obiektu
        modelTextField.setText(aktualnySamochod.getSilnik().getModel());

        // Silnik
        engineNameTextField.setText(aktualnySamochod.getSilnik().getModel());
        engineRpmTextField.setText(String.valueOf(aktualnySamochod.getSilnik().getObroty()));

        // Skrzynia
        gearboxNameTextField.setText(aktualnySamochod.getSkrzynia().getModel());
        gearboxGearTextField.setText(String.valueOf(aktualnySamochod.getSkrzynia().getAktualnyBieg()));

        // Samochód stan
        speedTextField.setText(aktualnySamochod.czyWlaczony() ? "Włączony" : "Wyłączony");
    }

    // ================== OBSŁUGA PRZYCISKÓW ==================

    private void onStartButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.wlacz();
        updateFields();
    }

    private void onStopButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.wylacz();
        updateFields();
    }

    private void onIncreaseGearButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.getSkrzynia().zwiekszBieg();
        updateFields();
    }

    private void onDecreaseGearButton() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.getSkrzynia().zmniejszBieg();
        updateFields();
    }

    private void onIncreaseRpm() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.getSilnik().zwiekszObroty(500);
        updateFields();
    }

    private void onDecreaseRpm() {
        if (aktualnySamochod == null) return;
        aktualnySamochod.getSilnik().zmniejszObroty(500);
        updateFields();
    }
}


