package products;

public class FoodProduct extends Product {
    public FoodProduct(String name, double weight) {
        super(name, weight);
    }

    @Override
    public String getType() {
        return "Продукт питания";
    }
}