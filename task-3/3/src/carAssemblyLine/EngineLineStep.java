package carAssemblyLine;

import assemblyLine.ILineStep;
import assemblyLine.IProductPart;

public class EngineLineStep implements ILineStep {
    
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Сборка двигателя автомобиля");
        return new Engine();
    }
}