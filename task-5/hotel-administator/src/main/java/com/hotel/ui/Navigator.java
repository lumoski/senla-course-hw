package com.hotel.ui;

import java.util.List;

public class Navigator {
    private Menu currentMenu;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        System.out.println("Menu: " + currentMenu.getName());
        List<MenuItem> items = currentMenu.getMenuItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getTitle());
        }

        System.out.println();
    }

    public void navigate(int index) {
        if (index < 1 || index > currentMenu.getMenuItems().size()) {
            System.out.println("Invalid option. Please select a valid menu item.");
            return;
        }

        MenuItem selectedItem = currentMenu.getMenuItems().get(index - 1);

        selectedItem.doAction();

        if (selectedItem.getNextMenu() != null) {
            setCurrentMenu(selectedItem.getNextMenu());
        }
    }
}
