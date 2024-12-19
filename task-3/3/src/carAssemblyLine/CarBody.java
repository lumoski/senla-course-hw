package carAssemblyLine;

import assemblyLine.IProductPart;

public class CarBody implements IProductPart {
    
    @Override
    public String getName() {
        return "Кузов";
    }
}