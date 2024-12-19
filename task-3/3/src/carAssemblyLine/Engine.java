package carAssemblyLine;

import assemblyLine.IProductPart;

public class Engine implements IProductPart {
    
    @Override
    public String getName() {
        return "Двигатель";
    }
}