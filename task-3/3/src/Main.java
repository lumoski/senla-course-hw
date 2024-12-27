import assemblyLine.IAssemblyLine;
import assemblyLine.ILineStep;
import assemblyLine.IProduct;

import carAssemblyLine.BodyLineStep;
import carAssemblyLine.Car;
import carAssemblyLine.CarAssemblyLine;
import carAssemblyLine.ChassisLineStep;
import carAssemblyLine.EngineLineStep;

public class Main {
    public static void main(String[] args) {
        ILineStep bodyStep = new BodyLineStep();
        ILineStep chassisStep = new ChassisLineStep();
        ILineStep engineStep = new EngineLineStep();

        IAssemblyLine carAssemblyLine = new CarAssemblyLine(bodyStep, chassisStep, engineStep);

        IProduct car = new Car();

        carAssemblyLine.assembleProduct(car);

        car.showProduct();
    }
}
