package symulator;

public class Komponent {
    protected String producent;
    protected String model;

    public Komponent(String producent, String model) {
        this.producent = producent;
        this.model = model;
    }
    String get_Producent() {
        return producent;
    }
    String get_Model() {
        return model;
    }
}
