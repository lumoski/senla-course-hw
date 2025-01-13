package com.hotel.controller.console.ui;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Menu {
    private String name;
    private List<MenuItem> menuItems = new ArrayList<>();

    public Menu addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        return this;
    }
}
