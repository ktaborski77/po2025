package org.example.samochodgui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.samochod.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloController {

    private Samochod mojSamochod;
    private Map<String, Samochod> garaz = new HashMap<>();
    private AnimationTimer drivingTimer;

    @FXML private ComboBox<String> carComboBox;
    @FXML private ImageView carImageView;
    @FXML private TextField modelTextField, plateTextField, weightTextField, speedTextField;
    @FXML private TextField gearboxNameTextField, gearboxPriceTextField, gearboxWeightTextField, gearboxGearTextField;
    @FXML private TextField engineNameTextField, enginePriceTextField, engineWeightTextField, engineRpmTextField;
    @FXML private TextField clutchNameTextField, clutchPriceTextField, clutchWeightTextField, clutchStateTextField;

    @FXML
    public void initialize() {
        try {
            Image carImage = new Image(getClass().getResource("/org/example/samochodgui/car-icon.jpg").toExternalForm());
            carImageView.setImage(carImage);
        } catch (Exception e) {
            System.out.println("Nie znaleziono pliku graficznego.");
        }

        Silnik s1 = new Silnik("Ferrari", "V8", 200, 50000, 8000, 0);
        Sprzeglo sp1 = new Sprzeglo("Bosch", "Sport", 10, 2000, false);
        SkrzyniaBiegow sk1 = new SkrzyniaBiegow("ZF", "Manual 6", 50, 8000, 0, 6, sp1);
        Samochod auto1 = new Samochod("Ferrari 488", "K1 FERRARI", 1400, s1, sk1, new Pozycja(20, 50));

        garaz.put(auto1.getModel(), auto1);
        updateComboBox();

        drivingTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (mojSamochod != null) {
                    mojSamochod.jedz();
                    carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
                    refresh();
                }
            }
        };
    }

    public void refresh() {
        if (mojSamochod == null) {
            clearFields();
            return;
        }

        modelTextField.setText(mojSamochod.getModel());
        plateTextField.setText(mojSamochod.getNrRejestracyjny());
        weightTextField.setText(String.valueOf(mojSamochod.getWaga()));
        speedTextField.setText(String.format("%.2f", mojSamochod.getPredkosc()));

        Silnik s = mojSamochod.getSilnik();
        engineNameTextField.setText(s.getProducent() + " " + s.getModel());
        engineRpmTextField.setText(String.valueOf(s.getObroty()));
        engineWeightTextField.setText(String.valueOf(s.getWaga()));
        enginePriceTextField.setText(String.valueOf(s.getCena()));

        SkrzyniaBiegow sk = mojSamochod.getSkrzynia();
        gearboxNameTextField.setText(sk.getProducent() + " " + sk.getModel());
        gearboxGearTextField.setText(String.valueOf(sk.getAktualnyBieg()));
        gearboxWeightTextField.setText(String.valueOf(sk.getWaga()));
        gearboxPriceTextField.setText(String.valueOf(sk.getCena()));

        Sprzeglo sp = sk.getSprzeglo();
        clutchNameTextField.setText(sp.getProducent() + " " + sp.getModel());
        clutchStateTextField.setText(sp.isStanSprzegla() ? "Wciśnięte" : "Zwolnione");
        clutchWeightTextField.setText(String.valueOf(sp.getWaga()));
        clutchPriceTextField.setText(String.valueOf(sp.getCena()));
    }

    private void clearFields() {
        modelTextField.clear(); plateTextField.clear(); weightTextField.clear(); speedTextField.clear();
        engineNameTextField.clear(); engineRpmTextField.clear(); engineWeightTextField.clear(); enginePriceTextField.clear();
        gearboxNameTextField.clear(); gearboxGearTextField.clear(); gearboxWeightTextField.clear(); gearboxPriceTextField.clear();
        clutchNameTextField.clear(); clutchStateTextField.clear(); clutchWeightTextField.clear(); clutchPriceTextField.clear();
    }

    private void updateComboBox() {
        carComboBox.getItems().clear();
        carComboBox.getItems().addAll(garaz.keySet());
    }

    @FXML
    public void openAddCarWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DodajSamochod.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Dodaj nowy samochód");
        DodajSamochodController controller = loader.getController();
        controller.setMainController(this);
        stage.show();
    }

    public void addCarToList(Samochod s) {
        garaz.put(s.getModel(), s);
        updateComboBox();
    }

    @FXML
    protected void onDeleteCarButton() {
        String selected = carComboBox.getValue();
        if (selected != null) {
            drivingTimer.stop();
            garaz.remove(selected);
            mojSamochod = null;
            updateComboBox();
            refresh();
            carImageView.setLayoutX(20);
        }
    }

    @FXML
    protected void onCloseMenu() {
        Platform.exit();
    }

    @FXML
    protected void onCarSelection(ActionEvent event) {
        String selected = carComboBox.getValue();
        if (selected != null) {
            mojSamochod = garaz.get(selected);
            carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
            refresh();
        }
    }

    @FXML protected void onStartButton() { if (mojSamochod != null) { mojSamochod.wlacz(); refresh(); } }
    @FXML protected void onStopButton() { if (mojSamochod != null) { mojSamochod.wylacz(); drivingTimer.stop(); refresh(); } }
    @FXML protected void onDriveButton() { if (mojSamochod != null) drivingTimer.start(); }
    @FXML protected void onIncreaseGearButton() { if (mojSamochod != null) { mojSamochod.getSkrzynia().zwiekszBieg(); refresh(); } }
    @FXML protected void onDecreaseGearButton() { if (mojSamochod != null) { mojSamochod.getSkrzynia().zmniejszBieg(); refresh(); } }
    @FXML protected void onIncreaseRpmButton() { if (mojSamochod != null) { mojSamochod.getSilnik().zwiekszObroty(); refresh(); } }
    @FXML protected void onDecreaseRpmButton() { if (mojSamochod != null) { mojSamochod.getSilnik().zmniejszObroty(); refresh(); } }
    @FXML protected void onPressClutchButton() { if (mojSamochod != null) { mojSamochod.getSkrzynia().getSprzeglo().wcisnij(); refresh(); } }
    @FXML protected void onReleaseClutchButton() { if (mojSamochod != null) { mojSamochod.getSkrzynia().getSprzeglo().zwolnij(); refresh(); } }
}