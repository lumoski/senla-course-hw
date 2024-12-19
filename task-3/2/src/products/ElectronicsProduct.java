package products;

public class ElectronicsProduct extends Product {
    public ElectronicsProduct(String name, double weight) {
        super(name, weight);
    }

    @Override
    public String getType() {
        return "Электроника";
    }
}