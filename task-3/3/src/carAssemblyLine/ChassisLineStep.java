package carAssemblyLine;

import assemblyLine.ILineStep;
import assemblyLine.IProductPart;

public class ChassisLineStep implements ILineStep {

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Сборка шасси автомобиля");
        return new Chassis();
    }
}