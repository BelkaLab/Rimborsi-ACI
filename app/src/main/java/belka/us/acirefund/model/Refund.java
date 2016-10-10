package belka.us.acirefund.model;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class Refund {
    String brand;
    String model;
    double kmCost;

    public Refund(String brand, String model, double kmCost) {
        this.brand = brand;
        this.model = model;
        this.kmCost = kmCost;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getKmCost() {
        return kmCost;
    }
}
