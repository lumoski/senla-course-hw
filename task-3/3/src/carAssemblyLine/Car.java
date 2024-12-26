package carAssemblyLine;

import assemblyLine.IProduct;
import assemblyLine.IProductPart;

public class Car implements IProduct {
    private IProductPart body;
    private IProductPart chassis;
    private IProductPart engine;

    @Override
    public void installFirstPart(IProductPart part) {
        this.body = part;
        System.out.println("Установлен: " + part.getName());
    }

    @Override
    public void installSecondPart(IProductPart part) {
        this.chassis = part;
        System.out.println("Установлен: " + part.getName());
    }

    @Override
    public void installThirdPart(IProductPart part) {
        this.engine = part;
        System.out.println("Установлен: " + part.getName());
    }

    @Override
    public void showProduct() {
        System.out.println("Автомобиль собран из: ");
        System.out.println("- " + body.getName());
        System.out.println("- " + chassis.getName());
        System.out.println("- " + engine.getName());
    }
}