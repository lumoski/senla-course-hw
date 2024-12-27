package carAssemblyLine;

import assemblyLine.IProductPart;

public class Chassis implements IProductPart {
    
    @Override
    public String getName() {
        return "Шасси";
    }
}