package org.example.samochodgui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.samochod.*;

import java.io.IOException;

// Implementacja interfejsu Listener
public class HelloController implements Listener {

    private Samochod mojSamochod;

    // Zmiana na ObservableList zgodnie z instrukcją
    private ObservableList<Samochod> garaz = FXCollections.observableArrayList();

    @FXML private ComboBox<Samochod> carComboBox;
    @FXML private ImageView carImageView;
    @FXML private AnchorPane mapPane; // Potrzebne do obsługi kliknięć

    @FXML private TextField modelTextField, plateTextField, weightTextField, speedTextField;
    @FXML private TextField gearboxNameTextField, gearboxPriceTextField, gearboxWeightTextField, gearboxGearTextField;
    @FXML private TextField engineNameTextField, enginePriceTextField, engineWeightTextField, engineRpmTextField;
    @FXML private TextField clutchNameTextField, clutchPriceTextField, clutchWeightTextField, clutchStateTextField;

    @FXML
    public void initialize() {
        try {
            // Upewnij się, że ścieżka do obrazka jest poprawna
            Image carImage = new Image(getClass().getResource("/org/example/samochodgui/car-icon.jpg").toExternalForm());
            carImageView.setImage(carImage);
        } catch (Exception e) {
            System.out.println("Nie znaleziono pliku graficznego.");
        }

        // Konfiguracja ComboBox
        carComboBox.setItems(garaz);

        // Obsługa wyboru z listy [cite: 69]
        carComboBox.setOnAction(event -> {
            Samochod selected = carComboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Jeśli mieliśmy wcześniej auto, odpinamy listenera
                if (mojSamochod != null) {
                    mojSamochod.removeListener(this);
                }
                mojSamochod = selected;
                // Przypinamy listenera do nowego auta [cite: 152]
                mojSamochod.addListener(this);

                // Ustawiamy obrazek w aktualnej pozycji auta
                carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
                carImageView.setLayoutY(mojSamochod.getAktualnaPozycja().getY());

                refresh();
            }
        });

        // Tworzenie przykładowego auta
        Silnik s1 = new Silnik("Ferrari", "V8", 200, 50000, 8000, 0);
        Sprzeglo sp1 = new Sprzeglo("Bosch", "Sport", 10, 2000, false);
        SkrzyniaBiegow sk1 = new SkrzyniaBiegow("ZF", "Manual 6", 50, 8000, 0, 6, sp1);
        Samochod auto1 = new Samochod("Ferrari 488", "K1 FERRARI", 1400, s1, sk1, new Pozycja(20, 50));

        // Dodanie do listy (metoda addListener jest wywoływana przy wyborze z ComboBoxa)
        garaz.add(auto1);
        carComboBox.getSelectionModel().selectFirst(); // To wyzwoli onAction i podepnie listenera

        // Obsługa myszki na mapie
        mapPane.setOnMouseClicked(event -> {
            if (mojSamochod != null) {
                double x = event.getX();
                double y = event.getY();
                // Wysyłamy auto do nowego celu [cite: 54]
                mojSamochod.jedzDo(new Pozycja(x, y));
            }
        });
    }

    // Metoda wymagana przez interfejs Listener [cite: 118]
    @Override
    public void update() {
        // Zmiany w GUI muszą być robione w wątku JavaFX [cite: 58]
        Platform.runLater(() -> {
            refresh();
            // Aktualizacja pozycji ikonki [cite: 60-61]
            if (mojSamochod != null) {
                carImageView.setLayoutX(mojSamochod.getAktualnaPozycja().getX());
                carImageView.setLayoutY(mojSamochod.getAktualnaPozycja().getY());
            }
        });
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

    // Metoda dodająca auto do ObservableList [cite: 79]
    public void addCarToList(Samochod s) {
        garaz.add(s);
        carComboBox.getSelectionModel().select(s); // Automatycznie wybiera nowe auto
    }

    @FXML
    protected void onDeleteCarButton() {
        if (mojSamochod != null) {
            mojSamochod.zakonczWatek(); // Zatrzymujemy wątek usuwanego auta
            mojSamochod.removeListener(this);
            garaz.remove(mojSamochod); // ObservableList automatycznie odświeży ComboBox [cite: 75]

            if (!garaz.isEmpty()) {
                carComboBox.getSelectionModel().selectFirst();
            } else {
                mojSamochod = null;
                clearFields();
                carImageView.setLayoutX(20);
                carImageView.setLayoutY(50);
            }
        }
    }

    @FXML
    protected void onCloseMenu() {
        // Zamykamy wątki wszystkich aut przed wyjściem
        for(Samochod s : garaz) {
            s.zakonczWatek();
        }
        Platform.exit();
    }

    // Metoda wymagana w carComboBox.setOnAction, ale pusta tutaj,
    // ponieważ logika jest wewnątrz listenera setOnAction w initialize
    @FXML protected void onCarSelection(ActionEvent event) {}

    // Metoda wyświetlająca błędy
    public void pokazBlad(String wiadomosc) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(wiadomosc);
        alert.showAndWait();
    }

    // Sterowanie - z obsługą błędów [cite: 11]
    @FXML protected void onStartButton() { if (mojSamochod != null) mojSamochod.wlacz(); }
    @FXML protected void onStopButton() { if (mojSamochod != null) mojSamochod.wylacz(); }

    // Drive button nie jest już potrzebny do AnimationTimer, ale może służyć jako reset "gazu"
    @FXML protected void onDriveButton() {
        // W tej implementacji ruch jest automatyczny w wątku Samochodu
    }

    @FXML protected void onIncreaseGearButton() {
        if (mojSamochod != null) {
            // Sprawdzenie czy sprzęgło jest wciśnięte (logika w SkrzyniaBiegow)
            // Ale możemy dodać walidację GUI tutaj jeśli chcemy rzucić Alert
            if (!mojSamochod.getSkrzynia().getSprzeglo().isStanSprzegla()) {
                pokazBlad("Wciśnij sprzęgło, aby zmienić bieg!");
            } else {
                mojSamochod.getSkrzynia().zwiekszBieg();
            }
        }
    }

    @FXML protected void onDecreaseGearButton() {
        if (mojSamochod != null) {
            if (!mojSamochod.getSkrzynia().getSprzeglo().isStanSprzegla()) {
                pokazBlad("Wciśnij sprzęgło, aby zmienić bieg!");
            } else {
                mojSamochod.getSkrzynia().zmniejszBieg();
            }
        }
    }

    @FXML protected void onIncreaseRpmButton() { if (mojSamochod != null) mojSamochod.getSilnik().zwiekszObroty(); }
    @FXML protected void onDecreaseRpmButton() { if (mojSamochod != null) mojSamochod.getSilnik().zmniejszObroty(); }
    @FXML protected void onPressClutchButton() { if (mojSamochod != null) mojSamochod.getSkrzynia().getSprzeglo().wcisnij(); }
    @FXML protected void onReleaseClutchButton() { if (mojSamochod != null) mojSamochod.getSkrzynia().getSprzeglo().zwolnij(); }
}