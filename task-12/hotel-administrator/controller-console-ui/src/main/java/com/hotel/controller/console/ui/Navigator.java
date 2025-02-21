package com.hotel.controller.console.ui;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Navigator {

    private Menu currentMenu;

    public void printMenu() {
        System.out.println("Menu: " + currentMenu.getName());
        List<MenuItem> items = currentMenu.getMenuItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getTitle());
        }

        System.out.println();
    }

    public boolean navigate(int index) {
        if (index < 1 || index > currentMenu.getMenuItems().size()) {
            System.out.println("Invalid option. Please select a valid menu item.");
            return false;
        }

        if (Objects.equals(currentMenu.getMenuItems().get(index - 1).getTitle(), "Exit")) {
            return true;
        }

        MenuItem selectedItem = currentMenu.getMenuItems().get(index - 1);

        selectedItem.doAction();

        if (selectedItem.getNextMenu() != null) {
            setCurrentMenu(selectedItem.getNextMenu());
        }

        return false;
    }
}
