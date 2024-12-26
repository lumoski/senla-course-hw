package products;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private List<Product> products = new ArrayList<>();
    private int capacity;

    public Warehouse(int capacity) {
        this.capacity = capacity;
    }

    public boolean addProduct(Product product) {
        if (products.size() >= capacity) {
            System.out.println("Склад переполнен! Невозможно добавить: " + product.getName());
            return false;
        }
        
        products.add(product);
        return true;
    }

    public double calculateTotalWeight() {
        return products
            .stream()
            .mapToDouble(products -> products.getWeight())
            .sum();
    }

    public void showProducts() {
        System.out.println("Товары на складе:");

        products
            .stream()
            .map(product -> product.getType() + " - " + product.getName() + " (" + product.getWeight() + " кг)")
            .forEach(System.out::println);
    }
}