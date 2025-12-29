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
        // Przygotowanie przykładowych silników
        Silnik s1 = new Silnik("VW", "1.9 TDI", 150, 2000, 5000, 0);
        Silnik s2 = new Silnik("Toyota", "Hybrid 2.0", 120, 15000, 6000, 0);
        Silnik s3 = new Silnik("Tesla", "Electric", 250, 30000, 15000, 0);

        dostepneSilniki.put(s1.getModel(), s1);
        dostepneSilniki.put(s2.getModel(), s2);
        dostepneSilniki.put(s3.getModel(), s3);
        engineComboBox.setItems(FXCollections.observableArrayList(dostepneSilniki.keySet()));

        // Przygotowanie przykładowych skrzyń biegów
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
                System.out.println("Wypełnij wszystkie pola!");
                return;
            }

            Silnik wybranySilnik = dostepneSilniki.get(selectedEngineKey);
            SkrzyniaBiegow wybranaSkrzynia = dostepneSkrzynie.get(selectedGearboxKey);

            Samochod nowy = new Samochod(model, registration, weight, wybranySilnik, wybranaSkrzynia, new Pozycja(20, 50));

            mainController.addCarToList(nowy);

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            System.out.println("Błąd: Waga musi być liczbą!");
        }
    }

    @FXML
    private void onCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}