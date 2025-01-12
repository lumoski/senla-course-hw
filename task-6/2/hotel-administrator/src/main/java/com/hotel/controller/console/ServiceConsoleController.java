package com.hotel.controller.console;

import com.hotel.controller.ServiceController;
import com.hotel.model.Service;
import com.hotel.service.HotelServiceService;
import com.hotel.utils.InputUtils;

public class ServiceConsoleController extends ServiceController {
    private static final String FILE_PATH = "services.csv";

    public ServiceConsoleController(HotelServiceService hotelServiceService) {
        super(hotelServiceService);
    }

    public Service addService() {
        System.out.println("Create a new Service");
    
        System.out.print("Enter Service ID (Long): ");
        Long id = InputUtils.readLong();
    
        System.out.print("Enter Service first name: ");
        String name = InputUtils.readString();

        System.out.print("Enter Service price (Long): ");
        double price = InputUtils.readDouble();

        System.out.print("Enter Service category: ");
        String category = InputUtils.readString();

        Service service = new Service(id, name, price, category);

        try {
            addService(service);
            
            System.out.println("\nService created successfully:");
            System.out.println(service);

            return service;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Service updateServicePrice() {
        System.out.print("Enter Service ID (Long): ");
        Long id = InputUtils.readLong();

        System.out.print("Enter new Service price (Long): ");
        double newPrice = InputUtils.readDouble();

        try {
            return updateServicePrice(id, newPrice);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void importFromCsv() {
        importFromCsv(FILE_PATH);
    }

    public void exportToCsv() {
        exportToCsv(FILE_PATH);
    }
}
