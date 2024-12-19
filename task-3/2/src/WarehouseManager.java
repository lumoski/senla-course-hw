import products.ClothingProduct;
import products.ElectronicsProduct;
import products.FoodProduct;
import products.Warehouse;

public class WarehouseManager {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(5);

        warehouse.addProduct(new FoodProduct("Яблоки", 10.5));
        warehouse.addProduct(new ElectronicsProduct("Ноутбук", 2.3));
        warehouse.addProduct(new ClothingProduct("Куртка", 1.5));
        warehouse.addProduct(new FoodProduct("Молоко", 1.0));
        warehouse.addProduct(new ElectronicsProduct("Телевизор", 8.0));

        warehouse.addProduct(new ClothingProduct("Джинсы", 1.2));

        warehouse.showProducts();

        double totalWeight = warehouse.calculateTotalWeight();
        System.out.printf("Общий вес всех товаров на складе: %.2f кг%n", totalWeight);
    }
}
