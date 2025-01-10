package com.hotel.controller.console;

import java.util.Scanner;

import com.hotel.controller.ServiceController;
import com.hotel.model.Service;
import com.hotel.service.HotelServiceService;

public class ServiceConsoleController extends ServiceController {
    private static final String FILE_PATH = "services.csv";
    private final Scanner scanner = InputManager.getInstance().getScanner();

    public ServiceConsoleController(HotelServiceService hotelServiceService) {
        super(hotelServiceService);
    }

    public Service addService() {
        System.out.println("Create a new Service");
    
        System.out.print("Enter Service ID (Long): ");
        Long id = scanner.nextLong();
        scanner.nextLine();
    
        System.out.print("Enter Service first name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Service price (Long): ");
        double price = scanner.nextDouble();

        System.out.print("Enter Service category: ");
        String category = scanner.nextLine();

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
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter new Service price (Long): ");
        double newPrice = scanner.nextDouble();

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
