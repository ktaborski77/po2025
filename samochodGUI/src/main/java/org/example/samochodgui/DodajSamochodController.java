package org.example.samochodgui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.samochod.*;

import java.util.HashMap;
import java.util.Map;

public class DodajSamochodController {

    @FXML private TextField modelTextField;
    @FXML private TextField registrationTextField;
    @FXML private TextField weightTextField;
    @FXML private ComboBox<String> engineComboBox;
    @FXML private ComboBox<String> gearboxComboBox;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    private HelloController mainController;
    private Map<String, Silnik> dostepneSilniki = new HashMap<>();
    private Map<String, SkrzyniaBiegow> dostepneSkrzynie = new HashMap<>();

    public void setMainController(HelloController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        // Przykładowe silniki
        Silnik s1 = new Silnik("VW", "1.9 TDI", 150, 2000, 5000, 0);
        Silnik s2 = new Silnik("Toyota", "Hybrid 2.0", 120, 15000, 6000, 0);
        Silnik s3 = new Silnik("Tesla", "Electric", 250, 30000, 15000, 0);

        dostepneSilniki.put(s1.getModel(), s1);
        dostepneSilniki.put(s2.getModel(), s2);
        dostepneSilniki.put(s3.getModel(), s3);
        engineComboBox.setItems(FXCollections.observableArrayList(dostepneSilniki.keySet()));

        // Ponieważ obiekty Skrzynia i Silnik są "zużywane" przez jeden samochód w relacji kompozycji,
        // w prawdziwej aplikacji tworzylibyśmy kopie. Tutaj dla uproszczenia (Lab) używamy tych samych referencji
        // lub tworzymy nowe w locie.

        Sprzeglo sp = new Sprzeglo("Standard", "S1", 5, 500, false);
        SkrzyniaBiegow sk1 = new SkrzyniaBiegow("Manual", "M5", 30, 1000, 0, 5, sp);
        SkrzyniaBiegow sk2 = new SkrzyniaBiegow("Auto", "DSG", 60, 5000, 0, 7, sp);

        dostepneSkrzynie.put(sk1.getModel(), sk1);
        dostepneSkrzynie.put(sk2.getModel(), sk2);
        gearboxComboBox.setItems(FXCollections.observableArrayList(dostepneSkrzynie.keySet()));
    }

    @FXML
    private void onConfirmButton() {
        try {
            String model = modelTextField.getText();
            String registration = registrationTextField.getText();
            double weight = Double.parseDouble(weightTextField.getText());

            String selectedEngineKey = engineComboBox.getValue();
            String selectedGearboxKey = gearboxComboBox.getValue();

            if (selectedEngineKey == null || selectedGearboxKey == null || model.isEmpty()) {
                mainController.pokazBlad("Wypełnij wszystkie pola!");
                return;
            }

            // Tworzymy nowe instancje komponentów na bazie wybranych (proste klonowanie dla bezpieczeństwa wątków)
            Silnik wzorzecSilnik = dostepneSilniki.get(selectedEngineKey);
            Silnik nowySilnik = new Silnik(wzorzecSilnik.getProducent(), wzorzecSilnik.getModel(), wzorzecSilnik.getWaga(), wzorzecSilnik.getCena(), 6000, 0);

            SkrzyniaBiegow wzorzecSkrzynia = dostepneSkrzynie.get(selectedGearboxKey);
            Sprzeglo noweSprzeglo = new Sprzeglo("Gen", "X", 10, 100, false);
            SkrzyniaBiegow nowaSkrzynia = new SkrzyniaBiegow(wzorzecSkrzynia.getProducent(), wzorzecSkrzynia.getModel(), wzorzecSkrzynia.getWaga(), wzorzecSkrzynia.getCena(), 0, 6, noweSprzeglo);

            // Tworzenie samochodu (automatycznie uruchamia jego wątek w konstruktorze)
            Samochod nowy = new Samochod(model, registration, weight, nowySilnik, nowaSkrzynia, new Pozycja(20, 50));

            mainController.addCarToList(nowy); // [cite: 79]

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            mainController.pokazBlad("Błąd: Waga musi być liczbą!");
        }
    }

    @FXML
    private void onCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}