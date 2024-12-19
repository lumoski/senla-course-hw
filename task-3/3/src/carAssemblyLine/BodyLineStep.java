package carAssemblyLine;

import assemblyLine.ILineStep;
import assemblyLine.IProductPart;

public class BodyLineStep implements ILineStep {
    
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Сборка кузова автомобиля");
        return new CarBody();
    }
}