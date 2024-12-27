package products;

public class ClothingProduct extends Product {
    public ClothingProduct(String name, double weight) {
        super(name, weight);
    }

    @Override
    public String getType() {
        return "Одежда";
    }
}